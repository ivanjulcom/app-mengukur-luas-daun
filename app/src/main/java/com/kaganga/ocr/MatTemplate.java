package com.kaganga.ocr;

import org.opencv.core.Mat;

public class MatTemplate {
    private String letter;
    private Mat mat;

    public MatTemplate(String letter, Mat mat) {
        this.letter = letter;
        this.mat = mat;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

}
