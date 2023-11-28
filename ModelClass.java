package com.example.smilingheart;

import android.graphics.Bitmap;

public class ModelClass {

    private String name, email, number;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    private Bitmap image;

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



    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ModelClass(String name, String email, String number, Bitmap image) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.number = number;
    }
}
