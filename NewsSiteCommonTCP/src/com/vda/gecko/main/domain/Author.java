package com.vda.gecko.main.domain;

/**
 * Created by 1 on 10/9/2015.
 */
public class Author {
    private int age;
    private String name;

    private int id;

    public Author(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
