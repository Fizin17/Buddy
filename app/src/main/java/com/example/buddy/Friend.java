package com.example.buddy;

public class Friend {
    private String name;
    private String gender;
    private String dob;
    private String phone;
    private String email;

    public Friend(String name, String gender, String dob, String phone, String email) {
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    // Getters
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
