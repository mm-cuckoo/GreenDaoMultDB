#### 前言
这是一篇介绍如何使用greenDao框架实现分库的文章。如果你对greenDao如何使用还不是很了解，下面有几篇文章推荐：   
https://blog.csdn.net/qq_35956194/article/details/79167897     
https://www.jianshu.com/p/4e6d72e7f57a   
https://blog.csdn.net/poorkick/article/details/55271660    

#### 解决问题
如果要使用greenDao实现分库，要解决下面两个问题：
1. 如何实现在指定位置创建数据库
2. 如何在指定数据库中创建对应的表

#### 实现在指定位置创建数据库
通过对greenDao源码分析，知道`DatabaseOpenHelper`是继承`SQLiteOpenHelper`实现对数据库操作的。那么``是如何获取数据库路径的， 看下面部分源码：

```
private SQLiteDatabase getDatabaseLocked(boolean writable) {
   .... // 省略部分代码
    try {
        if (DEBUG_STRICT_READONLY && !writable) {
            final String path = mContext.getDatabasePath(mName).getPath();
            db = SQLiteDatabase.openDatabase(path, mFactory,
                    SQLiteDatabase.OPEN_READONLY, mErrorHandler);
        } else {
            db = mContext.openOrCreateDatabase(mName, mEnableWriteAheadLogging ?
                    Context.MODE_ENABLE_WRITE_AHEAD_LOGGING : 0,
                    mFactory, mErrorHandler);
        }
    } catch (SQLiteException ex) {
        if (writable) {
            throw ex;
        }
        Log.e(TAG, "Couldn't open " + mName
                + " for writing (will try read-only):", ex);
        final String path = mContext.getDatabasePath(mName).getPath();
        db = SQLiteDatabase.openDatabase(path, mFactory,
                SQLiteDatabase.OPEN_READONLY, mErrorHandler);
    }
    ....  // 省略部分代码
     
}

```
在上面代码中`final String path = mContext.getDatabasePath(mName).getPath();` 这行代码就是获取数据库路径的。也就是通过`Content` 获取的数据库路径。如果在调用`getDatabasePath(mName)`方法时，把返回的路劲替换就可以了。思路是这样。

那么在什么地方可以干预`getDatabasePath(mName)`方法返回内容呢？最后发现`Application` 这个类继承 `ContextWrapper`, 如下：

```
public class Application extends ContextWrapper implements ComponentCallbacks2
```
那么就可以在应用的`Application`的继承实现类中重写`getDatabasePath(mName)` 方法， 如下：

```
public class App extends Application {

    @Override
    public File getDatabasePath(String name) {
        return super.getDatabasePath(name);
    }
}
```
只要在这个位置根据数据库名称返回相应位置就哦了。

其实不仅仅greenDao可以使用这种方式重写指定数据库位置，只要是通过`SQLiteOpenHelper`都可以。

#### 如何在指定数据库中创建对应的表
下面的介绍都是基于一个示例介绍的， 示例结构为，两个数据库(**db_a**和**db_b**)和四张表（**TABLE_A_A，TABLE_A_B，TABLE_B_A，TABLE_B_B**），    
> **db_a** 中有**TABLE_A_A，TABLE_A_B**表    
> **db_b** 中有**TABLE_B_A，TABLE_B_B**表

解决这个问题，要先从greenDao 生成的`DaoMaster`入手，看过`DaoMater`代码的都会知道， 创建表和删除就在这个类中的`createAllTables`和`dropAllTables` 静态方法进行。代码如下：

```
public static void createAllTables(Database db, boolean ifNotExists) {
    TableBaDao.createTable(db, ifNotExists);
    TableAaDao.createTable(db, ifNotExists);
    TableBbDao.createTable(db, ifNotExists);
    TableAbDao.createTable(db, ifNotExists);
}

public static void dropAllTables(Database db, boolean ifExists) {
    TableBaDao.dropTable(db, ifExists);
    TableAaDao.dropTable(db, ifExists);
    TableBbDao.dropTable(db, ifExists);
    TableAbDao.dropTable(db, ifExists);
}
```
如果使用这个`DaoMaster`中的这两个方法就会在每个库中都创建所这四张表，这不是想要的。
在往下看， 下面有一个静态内部抽象类`OpenHelper`, 源码如下：

```
public static abstract class OpenHelper extends DatabaseOpenHelper {
    public OpenHelper(Context context, String name) {
        super(context, name, SCHEMA_VERSION);
    }

    public OpenHelper(Context context, String name, CursorFactory factory) {
        super(context, name, factory, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(Database db) {
        createAllTables(db, false);
    }
}
```
看到这里，在`onCreate` 中调用了`createAllTables` 方法在这个`db`下创建这个表。有了这些信息，就可以解决在不同的数据库中创建不同的表了。只要对每个数据库自定义一个master类，然后继承greenDao 生成的`DaoMaster`,重写`createAllTables` ， `dropAllTables`方法， 在其中重写实现`DatabaseOpenHelper`， 在`onCreate` 中调用 `createAllTables` 创建对应的表。  
说了这么多，可能已经晕了， 贴个示例代码：

```
public class DatabaseADaoMaster extends DaoMaster{
    public DatabaseADaoMaster(SQLiteDatabase db) {
        super(db);
    }

    public DatabaseADaoMaster(Database db) {
        super(db);
    }

    public static void createAllTables(Database db, boolean ifNotExists) {
        TableAaDao.createTable(db, ifNotExists);
        TableAbDao.createTable(db, ifNotExists);

    }

    public static void dropAllTables(Database db, boolean ifExists) {
        TableAaDao.dropTable(db, ifExists);
        TableAbDao.dropTable(db, ifExists);
    }

    public static class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            createAllTables(db, false);
        }
    }
}
```

上面就greenDao分库实现的方法介绍.

上面示例工程使用GreenDao实现分库功能，对部分内容进行封装。

示例内容：
1. 多库路径统一管理
2. 统一管理不同库的Session
3. 使用MigrationHelper升级数据库

