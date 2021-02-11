package com.example.ecommerce.model;

public class Users
{
    private String Name,Password ,image,address;
    private String Phone;

    public Users()
    {

    }

    public Users(String name, String password, String image, String address, String phone) {
        Name = name;
        Password = password;
        this.image = image;
        this.address = address;
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
