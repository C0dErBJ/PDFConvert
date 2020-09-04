# PDFConvert

## 功能
1. 支持pdf导出文字，图片以及表格导出
2. 支持问题字体，颜色，大小格式导出
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
            executor.onComplete(factList -> {
                //解析结果
            });
            String uuid = executor.doExecutor(parser, exporter);
            executor.getExportFile(uuid);
        }
```