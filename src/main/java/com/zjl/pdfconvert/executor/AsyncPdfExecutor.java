package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.exporter.ExportFileModel;
import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Zhu jialiang
 * @date 2020/8/19
 */
public class AsyncPdfExecutor implements AsyncExecutor {
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> workQueue;
    private HashMap<String, ExportFileModel> result = new HashMap<>();


    public AsyncPdfExecutor() {
        this.init();
    }

    private void init() {
        workQueue = new ArrayBlockingQueue<Runnable>(6);
        executor = new ThreadPoolExecutor(2, 5, 60,
                TimeUnit.SECONDS, workQueue, r -> {
            Thread localThread = new Thread(r, "PDF-Parser");
            localThread.setUncaughtExceptionHandler((t, e) -> {
                synchronized (System.out) {
                    System.out.println(e.getMessage());
                }
            });
            return localThread;
        });

    }

    @Override
    public String doExecutor(Parser parser, Exporter exporter) {
        BlockingDeque<Fact> factBlockingDeque = new LinkedBlockingDeque<>();
        parser.setFactBlockingDeque(factBlockingDeque);
        exporter.setFactBlockingDeque(factBlockingDeque);
        String uuid = UUID.randomUUID().toString();
        exporter.setUniqueId(uuid);
        CompletableFuture.runAsync(parser, executor);
        CompletableFuture.runAsync(exporter, executor).whenComplete((unused, throwable) -> {
            ByteArrayOutputStream bos = exporter.writeByte();
            Path tempFile = null;
            try {
                tempFile = Files.createTempFile(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), parser.getFileName());
                Files.write(tempFile, bos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            ExportFileModel file = new ExportFileModel();
            file.setFileName(exporter.getFileName());
            file.setFilePath(tempFile.toAbsolutePath().toString());
            this.result.put(uuid, file);
        });
        return uuid;
    }

    @Override
    public ExportFileModel getExportFile(String uuid) {
        return this.result.get(uuid);
    }


}
