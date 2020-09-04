# PDFConvert


## 功能
1. 支持pdf导出文字，图片以及表格导出
2. 支持问题字体，颜色，大小导出
3. 支持图片同比例导出
4. 支持表格导出到word

## 使用
同步方法

``` java
        String importFile = "/test.pdf";
        try (InputStream is = PdfconvertApplicationTests.class.getResourceAsStream(importFile)) {
            Parser parser = new PdfParser();
            parser.setInputStream(is);
            Exporter exporter = new WordExporter();
            SimplePdfExecutor executor = new SimplePdfExecutor();
            byte[] result = executor.doExecutor(parser, exporter);
        } 
```

异步方法
``` java
        String importFile = "/test.pdf";
        try (InputStream is = PdfconvertApplicationTests.class.getResourceAsStream(importFile)) {
            Parser parser = new PdfParser();
            parser.setInputStream(is);
            Exporter exporter = new WordExporter();
            AsyncPdfExecutor executor = new AsyncPdfExecutor();
            String uuid = executor.doExecutor(parser, exporter);
            executor.getExportFile(uuid);
        }
```

## TODO
1. 文字导出支持高亮
2. 支持单独提取图片，表格数据
3. 支持表格导出excel
4. 支持表格导出excel附带表格样式
5. 支持字体加粗，倾斜，下划线
6. pdf目录解析