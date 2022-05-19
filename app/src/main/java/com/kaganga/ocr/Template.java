package com.kaganga.ocr;

public class Template {
    private int id;
    private String jenis, huruf, file;

    public Template() {
    }

    public Template(int id, String jenis, String huruf, String file) {
        this.id = id;
        this.jenis = jenis;
        this.huruf = huruf;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getHuruf() {
        return huruf;
    }

    public void setHuruf(String huruf) {
        this.huruf = huruf;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
