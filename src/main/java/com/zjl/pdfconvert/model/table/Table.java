package com.zjl.pdfconvert.model.table;

import com.zjl.pdfconvert.model.Element;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Table extends Element {
    private int rowCount;
    private int columnCount;
    private Cell[][] cells;


    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
}
