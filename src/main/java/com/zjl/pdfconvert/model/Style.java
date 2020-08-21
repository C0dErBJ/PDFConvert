package com.zjl.pdfconvert.model;

import java.awt.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Style {
    private String fontFamily = "";
    private float fontSize = 12;
    private boolean bold = false;
    private boolean italics = false;
    private boolean underLine = false;
    private String color;
    private float x;
    private float y;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
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
