package com.example.smilingheart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Smiling_Heart.db";
    public static final String TABLE_NAME = "community_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "ADDRESS";
    public static final String COL_4 = "PHONE_NO";


    Context context;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteImage;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        //create Database and Database Table
        SQLiteDatabase db = this.getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, ADDRESS TEXT, PHONE_NO INTEGER)"
        );
        db.execSQL("create table Donor(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, ADDRESS TEXT, PHONE_NO INTEGER, EMAIL TEXT, PASSWORD TEXT)"
        );
        db.execSQL("create table ProfileUser(name TEXT"+", email TEXT"+" , number TEXT "+", image BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS Donor");
        db.execSQL("DROP TABLE IF EXISTS ProfileUser");
        onCreate(db);
    }

    //Insert data to Community table
    public boolean insertData(String name, String address, String phone_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,address);
        contentValues.put(COL_4,phone_no);
        long result = db.insert(TABLE_NAME, null,contentValues);
        if (result ==-1)
            return false;
        else
            return true;
    }

    //View data from Community table
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select * from " + TABLE_NAME, null);
        return result;
    }

    //Update data in Community table
    public boolean updateData(String id, String name, String address, String phone_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,address);
        contentValues.put(COL_4,phone_no);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return true;
    }

    //Delete data from Community table
    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME, "ID = ?", new String[] {id});
    }

    //Insert data to Donor table
    public boolean insertData(String name, String address, String phone_no, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("address",address);
        contentValues.put("phone_no",phone_no);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long result = db.insert("Donor", null,contentValues);
        if (result ==-1)
            return false;
        else
            return true;
    }

    //Getting information where email equals
    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Donor where email = ?", new String[]{email});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    //Check email and password are valid
    public boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Donor where email =? and password =?", new String[]{email, password});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public void storeData(ModelClass modelClass){
        SQLiteDatabase database = this.getWritableDatabase();
        Bitmap bitmapImage = modelClass.getImage();

        byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteImage = byteArrayOutputStream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", modelClass.getName());
        contentValues.put("email", modelClass.getEmail());
        contentValues.put("number", modelClass.getNumber());
        contentValues.put("image", byteImage);


        long checkQuery = database.insert("ProfileUser", null, contentValues);
        if (checkQuery != -1){
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            database.close();
        } else {
            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor getUser(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from ProfileUser", null);
        return cursor;
    }

}
