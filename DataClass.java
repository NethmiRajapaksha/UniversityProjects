package com.example.smilingheart;

public class DataClass {

    private String dataTitle;
    private int dataDesc;
    private String dataCity;
    private int dataImg;

    public String getDataTitle() {
        return dataTitle;
    }

    public int getDataDesc() {
        return dataDesc;
    }

    public String getDataCity() {
        return dataCity;
    }

    public int getDataImg() {
        return dataImg;
    }

    public DataClass(String dataTitle, int dataDesc, String dataCity, int dataImg) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataCity = dataCity;
        this.dataImg = dataImg;
    }
}
