package com.zjl.pdfconvert.parser.table;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.table.Cell;
import com.zjl.pdfconvert.model.table.Table;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import com.zjl.pdfconvert.parser.Extractor;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zhu jialiang
 * @date 2020/8/25
 */
public class TableExtractor extends PDFGraphicsStreamEngine implements Extractor<Table> {
    private final GeneralPath linePath = new GeneralPath();
    private List<Table> tables;
    private SortedMap<Integer, List<Cell>> cells;
    private Integer pageIndex;
    private PDPage currentPage;

    private final AppendCellPath appendCellPath = new AppendCellPath();


    public TableExtractor() {
        super(null);
        addOperator(this.appendCellPath);
    }


    @Override
    public Integer getOrder() {
        return 3;
    }

    @Override
    public void doExtract(PDPage page, int pageIndex) throws IOException {
        this.pageIndex = pageIndex;
        this.currentPage = page;
        this.processPage(page);
    }

    @Override
    public void clearCache() {
        this.cells = null;
        this.tables = null;
        this.pageIndex = null;
        this.currentPage = null;
        this.appendCellPath.clearCache();
    }

    public boolean hasTable() {
        return !this.getCells().isEmpty();
    }

    public List<Table> getTables() {
        if (this.tables != null) {
            return tables;
        }
        if (!this.hasTable()) {
            return Collections.emptyList();
        }

        List<Table> newTables = new ArrayList<>(this.getCells().keySet().size());

        for (int i = 0; i < this.getCells().size(); i++) {
            List<Cell> cellList = this.getCells().get(i);
            int rowCount = cellList.stream().collect(Collectors.groupingBy(Cell::getY)).keySet().size();
            int columnCount = cellList.stream().collect(Collectors.groupingBy(Cell::getX)).keySet().size();
            Table table = new Table();
            table.initTable(cellList, rowCount, columnCount);
            newTables.add(table);
        }
        this.tables = newTables;
        return this.tables;
    }

    @Override
    public List<Table> pipeline(List<Fact> currentFacts) {
        this.concatWordCell(currentFacts, this.pageIndex);
        return this.getTables();
    }

    public List<Fact> concatWordCell(List<Fact> words, int pageNo) {
        SortedMap<Integer, List<Cell>> sortedCells = this.getCells();
        List<Fact> facts = new LinkedList<>();
        for (int i = 0; i < sortedCells.size(); i++) {
            List<Cell> cellList = sortedCells.get(i);
            for (Cell cell : cellList) {
                List<Word> within = words.stream().filter(a -> a instanceof Word
                        && (((Word) a).getStyle().getAbx() >= cell.getX() && ((Word) a).getStyle().getAbx() <= (cell.getX() + cell.getWidth()))
                        && ((Word) a).getStyle().getAby() <= (cell.getY() + cell.getHeight()) && ((Word) a).getStyle().getAby() >= cell.getY() && ((Word) a).getPageNo() == pageNo).
                        map(a -> (Word) a).collect(Collectors.toList());
                words.removeAll(within);
                List<LineStart> lss = words.stream().filter(a -> a instanceof LineStart && within.stream().anyMatch(b -> b == ((LineStart) a).getWord())).map(a -> (LineStart) a).collect(Collectors.toList());
                List<LineBreak> lbs = words.stream().filter(a -> a instanceof LineBreak && within.stream().anyMatch(b -> b == ((LineBreak) a).getWord())).map(a -> (LineBreak) a).collect(Collectors.toList());
                words.removeAll(lss);
                words.removeAll(lbs);
                cell.setWords(within);
                facts.addAll(within);
                facts.addAll(lss);
                facts.addAll(lbs);
            }
        }
        return facts;
    }

    public SortedMap<Integer, List<Cell>> getCells() {
        if (this.cells != null) {
            return this.cells;
        }
        this.cells = this.appendCellPath.getCells();
        return this.cells;
    }


    @Override
    public void appendRectangle(Point2D point2D, Point2D point2D1, Point2D point2D2, Point2D point2D3) {
// 不实现
    }

    @Override
    public void drawImage(PDImage pdi) {
        // 不实现
    }

    @Override
    public void clip(int windingRule) {
        // 不实现
    }

    @Override
    public void moveTo(float x, float y) {
        // 不实现
    }

    @Override
    public void lineTo(float x, float y) {
        // 不实现
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        // 不实现
    }

    @Override
    public Point2D getCurrentPoint() {
        return linePath.getCurrentPoint();
    }

    @Override
    public void closePath() {
        // 不实现
    }

    @Override
    public void endPath() {
        // 不实现
    }

    @Override
    public void strokePath() {
        // 不实现
    }

    @Override
    public void fillPath(int windingRule) {
        // 不实现
    }

    @Override
    public void fillAndStrokePath(int windingRule) {
        // 不实现
    }

    @Override
    public void shadingFill(COSName cosn) {
        // 不实现
    }
}