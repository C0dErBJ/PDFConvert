package com.zjl.pdfconvert.exporter;

import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Parser;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class SyncPdfExecutor implements PdfExecutor {
    private BlockingDeque<Fact> factBlockingDeque;
    private Parser pdfParser;
    private Exporter exporter;
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> workQueue;

    private String importFilePath;
    private String exportFilePath;
    private volatile boolean isParseFinished = false;
    private final Object _lock = new Object();

    public SyncPdfExecutor(Parser parser, Exporter exporter) {
        this.pdfParser = parser;
        this.exporter = exporter;
        this.factBlockingDeque = new LinkedBlockingDeque<>();
        this.init();
    }

    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
        this.pdfParser.setFilePath(importFilePath);
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
        this.exporter.setFilePath(exportFilePath);
    }

    private void init() {
        workQueue = new ArrayBlockingQueue<Runnable>(2);
        executor = new ThreadPoolExecutor(2, 5, 60,
                TimeUnit.SECONDS, workQueue, r -> {
            Thread localThread = new Thread(r, "PDF-Parser");
            localThread.setUncaughtExceptionHandler((t, e) -> {
                System.out.println(e.getMessage());
            });
            return localThread;
        });

    }

    public void start() {
        this.executor.submit(SyncPdfExecutor.this::doParse);
        this.executor.submit(SyncPdfExecutor.this::doExport);
    }

    @Override
    public void doExport() {
        while (true) {
            Fact fact = this.factBlockingDeque.pollFirst();
            this.exporter.export(fact);
            if (isParseFinished && this.factBlockingDeque.isEmpty()) {
                break;
            }
        }
        this.exporter.writeFile();
        this.executor.shutdown();
    }

    @Override
    public void doParse() {
        List<Fact> facts = this.pdfParser.parse();
        for (int i = 0; i < facts.size(); i++) {
            if (facts.get(i) == null) {
                System.out.println("队列内包含null值");
                System.out.println(i + "前值" + facts.get(i - 1));
            }
            this.factBlockingDeque.offerLast(facts.get(i));
        }
        facts.forEach(a -> {

        });
        this.isParseFinished = true;
    }


}
