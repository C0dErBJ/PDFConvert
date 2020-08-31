package com.zjl.pdfconvert.model;

import com.zjl.pdfconvert.model.word.FontName;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Style {
    private FontName fontFamily;
    private float fontSize = 12;
    private boolean bold = false;
    private boolean italics = false;
    private boolean underLine = false;
    private String color;
    private String backgroundColor;
    private int x;
    private int y;
    private int abx;
    private int aby;


    public float getAbx() {
        return abx;
    }

    public void setAbx(int abx) {
        this.abx = abx;
    }

    public int getAby() {
        return aby;
    }

    public void setAby(int aby) {
        this.aby = aby;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public FontName getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(FontName fontFamily) {
        this.fontFamily = fontFamily;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalics() {
        return italics;
    }

    public void setItalics(boolean italics) {
        this.italics = italics;
    }

    public boolean isUnderLine() {
        return underLine;
    }

    public void setUnderLine(boolean underLine) {
        this.underLine = underLine;
    }


}
