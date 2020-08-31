package com.zjl.pdfconvert.parser.table;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.table.Cell;
import com.zjl.pdfconvert.model.word.LineBreak;
import com.zjl.pdfconvert.model.word.LineStart;
import com.zjl.pdfconvert.model.word.Word;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Zhu jialiang
 * @date 2020/8/25
 */
public class HighlightExtractor extends PDFGraphicsStreamEngine {
    private final GeneralPath linePath = new GeneralPath();
    private TreeMap<Integer, List<Cell>> cells;

    private AppendCellPath appendCellPath = new AppendCellPath();


    public HighlightExtractor() {
        super(null);
        addOperator(this.appendCellPath);
    }




    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {

    }

    public void clearCache() {
        this.cells = null;
        this.appendCellPath.clearCache();
    }

    public boolean hasTable() {
        return !this.getCells().isEmpty();
    }


    public List<Fact> highlightWord(List<Fact> words, int pageNo) {
        TreeMap<Integer, List<Cell>> cells = this.getCells();
        List<Fact> facts = new LinkedList<>();
        for (int i = 0; i < cells.size(); i++) {
            List<Cell> cellList = cells.get(i);
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

    public TreeMap<Integer, List<Cell>> getCells() {
        if (this.cells != null) {
            return this.cells;
        }
        this.cells = this.appendCellPath.getCells();
        return this.cells;
    }


    @Override
    public void drawImage(PDImage pdi) throws IOException {
    }

    @Override
    public void clip(int windingRule) throws IOException {
    }

    @Override
    public void moveTo(float x, float y) throws IOException {
    }

    @Override
    public void lineTo(float x, float y) throws IOException {
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
    }

    @Override
    public Point2D getCurrentPoint() throws IOException {
        return linePath.getCurrentPoint();
    }

    @Override
    public void closePath() throws IOException {
    }

    @Override
    public void endPath() throws IOException {
    }

    @Override
    public void strokePath() throws IOException {
    }

    @Override
    public void fillPath(int windingRule) throws IOException {
    }

    @Override
    public void fillAndStrokePath(int windingRule) throws IOException {
    }

    @Override
    public void shadingFill(COSName cosn) throws IOException {
    }
}