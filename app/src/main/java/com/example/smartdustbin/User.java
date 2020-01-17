package com.example.smartdustbin;

public class User {
    private String name, last_name, email, code;

    public User() {
    }

    public User(String name, String last_name, String email, String code) {

        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_Name() {
        return last_name;
    }

    public void setLast_Name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) { this.code = code; }
}
