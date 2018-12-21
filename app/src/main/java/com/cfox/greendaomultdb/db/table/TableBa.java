package com.cfox.greendaomultdb.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "TABLE_B_A")
public class TableBa {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "NAME")
    private String name;
    @Property(nameInDb = "AGE")
    private int age;
    @Generated(hash = 973354888)
    public TableBa(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 2055548046)
    public TableBa() {
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
