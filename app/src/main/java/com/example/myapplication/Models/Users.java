package com.example.myapplication.Models;

public class Users {
    String uid,name,email,profileImage,pass;


    public Users(){

    }

    public Users(String uid, String name, String email, String profileImage) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public Users(String name, String email, String pass) {
        this.name = name;
        this.pass = pass;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
