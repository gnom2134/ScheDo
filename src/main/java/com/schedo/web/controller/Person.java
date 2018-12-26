package com.schedo.web.controller;

public class Person {

    public Person(String name, int id, String email) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private String name;
    private int id;
    private String email;
    private String password;
}
