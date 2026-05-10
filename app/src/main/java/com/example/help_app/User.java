package com.example.help_app;

public class User {
    public String name;
    public String blood;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String blood, String phone) {
        this.name = name;
        this.blood = blood;
        this.phone = phone;
    }
}