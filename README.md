# PDFConvert

## ����
1. ֧��pdf�������֣�ͼƬ�Լ���񵼳�
2. ֧���������壬��ɫ����С��ʽ����
3. ֧��ͼƬͬ��������
4. ֧�ֱ�񵼳���word

## ʹ��
ͬ������

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

�첽����
``` java
        String importFile = "/test.pdf";
        try (InputStream is = PdfconvertApplicationTests.class.getResourceAsStream(importFile)) {
            Parser parser = new PdfParser();
            parser.setInputStream(is);
            Exporter exporter = new WordExporter();
            AsyncPdfExecutor executor = new AsyncPdfExecutor();
            executor.onComplete(factList -> {
                //�������
            });
            String uuid = executor.doExecutor(parser, exporter);
            executor.getExportFile(uuid);
        }
```