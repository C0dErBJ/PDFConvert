package com.zjl.pdfconvert.model.table;

import com.zjl.pdfconvert.model.Element;
import com.zjl.pdfconvert.model.word.Word;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Cell extends Element {
    private Integer x;
    private Integer y;
    private float width;
    private float height;
    private List<Word> words;
    private int lineWidth;

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public String getText() {
        return this.words != null ? this.words.stream().map(Word::getText).collect(Collectors.joining()) : "";
    }


}
