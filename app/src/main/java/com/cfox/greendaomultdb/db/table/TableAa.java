package com.cfox.greendaomultdb.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;


@Entity(nameInDb = "TABLE_A_A")
public class TableAa {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "NAME")
    private String name;
    @Property(nameInDb = "AGE")
    private int age;
    @Generated(hash = 1395731094)
    public TableAa(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 1149729661)
    public TableAa() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
