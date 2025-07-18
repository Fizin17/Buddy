package com.example.buddy;

import java.io.Serializable;

public class Friend implements Serializable {
    private int id; // Add this
    private String name;
    private String gender;
    private String dob;
    private String phone;
    private String email;

    // Constructor with ID (for retrieving/updating from DB)
    public Friend(int id, String name, String gender, String dob, String phone, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    // Constructor without ID (optional, for adding new friends)
    public Friend(String name, String gender, String dob, String phone, String email) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Setters (optional, if needed)
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDob(String dob) { this.dob = dob; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
}
