package com.example.practicleone;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {

    public int IDUser;
    public String Name;
    public String FirstName;
    public String SecondName;
    public String PhoneNumber;
    public String Picture;


   public User (int IDUser,String Name, String FirstName, String SecondName, String PhoneNumber, String Picture)
    {
        this.IDUser=IDUser;
        this.Name=Name;
        this.FirstName=FirstName;
        this.SecondName=SecondName;
        this.PhoneNumber=PhoneNumber;
        this.Picture=Picture;

    }

    public int getId()
    {
        return IDUser;
    }

}
