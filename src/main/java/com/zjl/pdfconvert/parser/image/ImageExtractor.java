package com.zjl.pdfconvert.parser.image;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.model.Image;
import com.zjl.pdfconvert.model.Style;
import com.zjl.pdfconvert.parser.Extractor;
import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.DrawObject;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.contentstream.operator.OperatorName;
import org.apache.pdfbox.contentstream.operator.state.*;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageExtractor extends PDFStreamEngine implements Extractor<Image> {
    private List<Image> images = new ArrayList<>();
    private Integer pageIndex;
    private PDPage currentPage;

    public ImageExtractor() {
        addOperator(new Concatenate());
        addOperator(new DrawObject());
        addOperator(new SetGraphicsStateParameters());
        addOperator(new Save());
        addOperator(new Restore());
        addOperator(new SetMatrix());
    }

    public List<Image> getImages() {
        return images;
    }

    @Override
    public void clearCache() {
        this.pageIndex = null;
        this.currentPage = null;
        this.images = new ArrayList<>();
    }

    @Override
    public List<Image> pipeline(List<Fact> facts) {
        return this.getImages();
    }

    /**
     * This is used to handle an operation.
     *
     * @param operator The operation to perform.
     * @param operands The list of arguments.
     * @throws IOException If there is an error processing the operation.
     */
    @Override
    protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
        String operation = operator.getName();
        if (OperatorName.DRAW_OBJECT.equals(operation)) {
            COSName objectName = (COSName) operands.get(0);
            PDXObject xobject = getResources().getXObject(objectName);
            if (xobject instanceof PDImageXObject) {
                Image imageFact = new Image();
                PDImageXObject image = (PDImageXObject) xobject;
                imageFact.setHeight(image.getHeight());
                imageFact.setWidth(image.getWidth());
                imageFact.setFileName(objectName.getName());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image.getImage(), "PNG", baos);
                imageFact.setFile(baos.toByteArray());

                Matrix ctmNew = getGraphicsState().getCurrentTransformationMatrix();
                float imageXScale = ctmNew.getScalingFactorX();
                float imageYScale = ctmNew.getScalingFactorY();
                imageFact.setDisplayWidth(ctmNew.getScalingFactorX());
                imageFact.setDisplayHeight(ctmNew.getScalingFactorY());
                Style style = new Style();
                style.setX((int) ctmNew.getTranslateX());
                style.setY((int) ctmNew.getTranslateY());
                imageFact.setStyle(style);
                this.images.add(imageFact);
            } else if (xobject instanceof PDFormXObject) {
                PDFormXObject form = (PDFormXObject) xobject;
                showForm(form);
            }
        } else {
            super.processOperator(operator, operands);
        }
    }

    @Override
    public Integer getOrder() {
        return 2;
    }

    @Override
    public void doExtract(PDPage page, int pageIndex) throws IOException {
        this.currentPage = page;
        this.pageIndex = pageIndex;
        this.processPage(page);
    }
}