package com.example.ecommerce.model;

public class Cart
{
    private String Name, Price ,Quantity,pid,Discount;


    public Cart()
    {

    }
    public Cart(String name, String price, String quantity, String pid, String discount) {
        Name = name;
        Price = price;
        Quantity = quantity;
        this.pid = pid;
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
