package com.kaganga.ocr;

public class History {
    private int id;
    private String date, imageuri, result;

    public History() {
    }

    public History(int id, String date, String imageuri, String result) {
        this.id = id;
        this.date = date;
        this.imageuri = imageuri;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
