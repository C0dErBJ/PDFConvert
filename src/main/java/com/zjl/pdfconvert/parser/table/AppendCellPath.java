package com.zjl.pdfconvert.parser.table;

import com.zjl.pdfconvert.model.table.Cell;
import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.graphics.GraphicsOperatorProcessor;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/25
 */
public class AppendCellPath extends GraphicsOperatorProcessor {
    private List<Cell> currentCell = new LinkedList<>();
    private TreeMap<Integer, List<Cell>> preCells = new TreeMap<>();

    public AppendCellPath() {
    }

    public void clearCache() {
        this.currentCell = new LinkedList<>();
        this.preCells = new TreeMap<>();
    }

    @Override
    public void process(Operator operator, List<COSBase> operands) throws IOException {
        if (operands.size() < 4) {
            throw new MissingOperandException(operator, operands);
        } else if (this.checkArrayTypesClass(operands, COSNumber.class)) {
            COSNumber x = (COSNumber) operands.get(0);
            COSNumber y = (COSNumber) operands.get(1);
            COSNumber w = (COSNumber) operands.get(2);
            COSNumber h = (COSNumber) operands.get(3);
            float x1 = x.intValue();
            float y1 = y.intValue();
            float variance = this.context.getGraphicsState().getLineWidth();
            Point2D p0 = this.context.transformedPoint(x1, y1);
            if (w.intValue() == 0 || h.intValue() == 0) {
                return;
            }
            //标志一个table解析完成
            if (x1 == 0 && y1 == 0 && !this.currentCell.isEmpty()) {
                this.preCells.put(this.preCells.size(), currentCell);
                this.currentCell = new LinkedList<>();
                return;
            }
            Optional<Cell> existCell = this.currentCell.stream().filter(a -> (within(a.getX(), p0.getX(), variance)
                    && within(a.getY(), p0.getY(), variance))).findFirst();
            if (!existCell.isPresent() && x1 != 0 && y1 != 0) {
                Cell cell = new Cell();
                cell.setX((int) p0.getX());
                cell.setY((int) p0.getY());
                cell.setWidth(w.intValue());
                cell.setHeight(h.intValue());
                //这个线宽其实有问题，取得只是连接左下角点的线，不一定准确
                cell.setLineWidth((int) variance);
                currentCell.add(cell);
            }

        }
    }

    public boolean within(double first, double second, float variance) {
        return second <= first + variance && second >= first - variance;
    }

    @Override
    public String getName() {
        return "re";
    }

    public SortedMap<Integer, List<Cell>> getCells() {
        TreeMap<Integer, List<Cell>> cells = new TreeMap<>();
        for (int i = 0; i < this.preCells.size(); i++) {
            List<Cell> cell = this.preCells.get(i);
            if (cell.size() < 4) {
                continue;
            }
            cell.sort((o1, o2) -> o1.getY().equals(o2.getY())
                    ? o2.getX().compareTo(o1.getX())
                    : o1.getY().compareTo(o2.getY()));
            Cell rightBottom = cell.get(0);
            Cell leftTop = cell.get(cell.size() - 1);

            cell.sort((o1, o2) -> o1.getY().equals(o2.getY())
                    ? o2.getX().compareTo(o1.getX())
                    : o2.getY().compareTo(o1.getY()));
            Cell rightTop = cell.get(0);
            Cell leftBottom = cell.get(cell.size() - 1);

            if (rightBottom.getX().equals(rightTop.getX())
                    && rightBottom.getY().equals(leftBottom.getY())
                    && leftBottom.getX().equals(leftTop.getX())
                    && leftBottom.getY().equals(rightBottom.getY())) {
                cell.sort((o1, o2) -> o1.getY().equals(o2.getY())
                        ? o1.getX().compareTo(o2.getX())
                        : o2.getY().compareTo(o1.getY()));
                cells.put(cells.size(), cell);
            }
        }
        return cells;
    }


}