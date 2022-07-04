package com.example.practicleone.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.practicleone.User;

import java.util.ArrayList;
import java.util.List;

public class MyDBManager {
    private Context context;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;
    User user;

    public MyDBManager (Context context){
        this.context=context;
        myDBHelper=new MyDBHelper(context);
    }

    public void openDB(){
        db=myDBHelper.getWritableDatabase();
    }

    public void insertDB(String Name,String FirstName,String SecondName,String PhoneNumber,String Picture){
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.Name,Name);
        cv.put(MyConstants.FirstName,FirstName);
        cv.put(MyConstants.SecondName,SecondName);
        cv.put(MyConstants.PhoneNumber,PhoneNumber);
        cv.put(MyConstants.Picture,Picture);
        db.insert(MyConstants.TABLE_NAME,null,cv);
    }

    public List<String> readfromDBforList(){
        List<String> lictClientFromDB = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            String Name = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.Name));
            String FirstName = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.FirstName));
            String SecondName = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.SecondName));

            String ClientForList=FirstName+" "+Name+" "+SecondName;

            lictClientFromDB.add(ClientForList);
        }
        cursor.close();
        return lictClientFromDB;
    }

    public User readPersonalInf(String Family, String NameUser){

        Cursor cursor = db.rawQuery("select * FROM Contact where FirstName = '"+Family+"' AND Name ='"+ NameUser+"'", null);
        while (cursor.moveToNext()){
            String Name = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.Name));
            String FirstName = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.FirstName));
            String SecondName = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.SecondName));
            String PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.PhoneNumber));
            String Picture = cursor.getString(cursor.getColumnIndexOrThrow(MyConstants.Picture));

           user = new User(Name,FirstName,SecondName,PhoneNumber,Picture);


        }
        cursor.close();
        return user;
    }

    public void DeleteFromDB(String Family, String NameUser){
        db.execSQL("DELETE FROM Contact where FirstName = '"+Family+"' AND Name ='"+ NameUser+"'");

    }

    public boolean CheckUser(String Family, String NameUser, String SecondName, String PhoneNumber){
        Cursor cursor = db.rawQuery("select * FROM Contact where Name ='"+ NameUser+"' AND SecondName ='"+ SecondName +"' AND FirstName ='"+ Family +"'", null);
        if (cursor.getCount()!=0){
            cursor.close();
            return true;
        }

        else{
            cursor.close();
        return false;}

    }

    public void UpdateUserINF(String Family, String NameUser, String SecondName, String PhoneNumber){
        db.execSQL("UPDATE Contact SET FirstName = '"+Family+"' , Name ='"+ NameUser+"' , SecondName ='"+ SecondName +"' , PhoneNumber ='" + PhoneNumber +"'  where Name ='"+ NameUser+"' AND SecondName ='"+ SecondName +"' AND FirstName ='"+ Family +"'\"");
    }

    public void closeDB(){
        myDBHelper.close();
    }

}
