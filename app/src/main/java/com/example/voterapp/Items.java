package com.example.voterapp;

public class Items {
    String pollName;
    String phone;
    public  Items(){ }
    public Items(String pollName, String phone){
        this.pollName = pollName;
        this.phone = phone;
    }
    public void setPollName(String pollName){
        this.pollName = pollName;
    }
    public void setPhone(String phone){ this.phone = phone; }
    public String getPollName(){
        return this.pollName;
    }
    public String getPhone() { return  this.phone; }
}
