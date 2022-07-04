package com.example.practicleone.db;

public class MyConstants {
    public static final String TABLE_NAME = "Contact";
    public static final String _ID = "_id";
    public static final String Name = "Name";
    public static final String FirstName = "FirstName";
    public static final String SecondName = "SecondName";
    public static final String PhoneNumber = "PhoneNumber";
    public static final String Picture = "Picture";
    public static final String DB_NAME = "my_contact.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_STRUCTURE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    Name + " TEXT," +
                    FirstName + " TEXT," +
                    SecondName + " TEXT," +
                    PhoneNumber + " TEXT," +
                    Picture + " TEXT)";
    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
