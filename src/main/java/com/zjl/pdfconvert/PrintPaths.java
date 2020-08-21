package com.zjl.pdfconvert;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;
import org.apache.pdfbox.text.TextPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * @author Zhu jialiang
 * @date 2020/8/17
 */
public class PrintPaths extends PDFGraphicsStreamEngine {
    private final GeneralPath linePath = new GeneralPath();
    private int clipWindingRule = -1;

    public PrintPaths(PDPage page) {
        super(page);
    }

    public static void main(String[] args) throws IOException {
        try (PDDocument document = PDDocument.load(new File("C:\\Users\\Zhu jialiang\\Desktop\\参数.pdf"))) {
            PDPage page = document.getPage(0);
            PrintPaths test = new PrintPaths(page);
            test.processPage(page);
        }
    }

    @Override
    public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {
        // to ensure that the path is created in the right direction, we have to create
        // it by combining single lines instead of creating a simple rectangle
        linePath.moveTo((float) p0.getX(), (float) p0.getY());
        System.out.println("move To " + p0.getX() + "," + p0.getY());
        linePath.lineTo((float) p1.getX(), (float) p1.getY());
        System.out.println("line To " + p1.getX() + "," + p1.getY());
        linePath.lineTo((float) p2.getX(), (float) p2.getY());
        System.out.println("line To " + p2.getX() + "," + p2.getY());
        linePath.lineTo((float) p3.getX(), (float) p3.getY());
        System.out.println("line To " + p3.getX() + "," + p3.getY());


        // close the subpath instead of adding the last line so that a possible set line
        // cap style isn't taken into account at the "beginning" of the rectangle
        linePath.closePath();
    }

    @Override
    public void drawImage(PDImage pdi) throws IOException {
    }

    @Override
    public void clip(int windingRule) throws IOException {
        // the clipping path will not be updated until the succeeding painting operator is called
        clipWindingRule = windingRule;

    }

    @Override
    public void moveTo(float x, float y) throws IOException {
        linePath.moveTo(x, y);
        System.out.println("moveTo");
    }

    @Override
    public void lineTo(float x, float y) throws IOException {
        linePath.lineTo(x, y);
        System.out.println("lineTo");
    }

    @Override
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
        linePath.curveTo(x1, y1, x2, y2, x3, y3);
        System.out.println("curveTo");
    }

    @Override
    public Point2D getCurrentPoint() throws IOException {
        return linePath.getCurrentPoint();
    }

    @Override
    public void closePath() throws IOException {
        linePath.closePath();
    }

    @Override
    public void endPath() throws IOException {
        if (clipWindingRule != -1) {
            linePath.setWindingRule(clipWindingRule);
            getGraphicsState().intersectClippingPath(linePath);
            clipWindingRule = -1;
        }
        linePath.reset();

    }

    @Override
    public void strokePath() throws IOException {
        // do stuff
        System.out.println("你好:" + linePath.getBounds2D());

        linePath.reset();
    }

    @Override
    public void fillPath(int windingRule) throws IOException {
        linePath.reset();
    }

    @Override
    public void fillAndStrokePath(int windingRule) throws IOException {
        linePath.reset();
    }

    @Override
    public void shadingFill(COSName cosn) throws IOException {
    }
}
