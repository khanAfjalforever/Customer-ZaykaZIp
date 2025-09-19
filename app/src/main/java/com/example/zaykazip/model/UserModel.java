package com.example.zaykazip.model;

public class UserModel {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

    // Empty constructor required by Firebase
    public UserModel() {
    }

    // ✅ Constructor used in SignActivity
    public UserModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ✅ Full constructor (for Profile editing or later use)
    public UserModel(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}
