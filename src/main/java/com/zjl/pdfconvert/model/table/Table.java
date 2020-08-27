package com.zjl.pdfconvert.model.table;

import com.zjl.pdfconvert.model.Element;

import java.util.List;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class Table extends Element {
    private int rowCount;
    private int columnCount;
    private Cell[][] cells;


    public void initTable(List<Cell> cells, int rowCount, int columnCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.cells = new Cell[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                this.cells[i][j] = cells.get(5 * i + j);
            }
        }
    }


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
