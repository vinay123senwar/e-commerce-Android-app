package com.example.ecommerce.Admin;

public class AdminOrders
{
    private String Name,Phone,Address,City,State,Date,Time,TotalAmount;

public AdminOrders()
    {

    }

    public AdminOrders(String name, String phone, String address, String city, String state, String date, String time, String totalAmount) {
        Name = name;
        Phone = phone;
        Address = address;
        City = city;
        State = state;
        Date = date;
        Time = time;
        TotalAmount = totalAmount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
