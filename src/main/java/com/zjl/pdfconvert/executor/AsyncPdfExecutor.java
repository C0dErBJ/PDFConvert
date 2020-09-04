package com.zjl.pdfconvert.executor;

import com.zjl.pdfconvert.exporter.ExportFileModel;
import com.zjl.pdfconvert.exporter.Exporter;
import com.zjl.pdfconvert.model.Fact;
import com.zjl.pdfconvert.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPdfExecutor.class);
    private ThreadPoolExecutor executor;
    private final HashMap<String, ExportFileModel> result = new HashMap<>();
    private ParseCompleteListener parseCompleteListener;


    public AsyncPdfExecutor() {
        this.init();
    }

    private void init() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(6);
        executor = new ThreadPoolExecutor(2, 5, 60,
                TimeUnit.SECONDS, workQueue, r -> {
            Thread localThread = new Thread(r, "PDF-Parser");
            localThread.setUncaughtExceptionHandler((t, e) -> LOGGER.error("异步执行发生异常", e));
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
        CompletableFuture.runAsync(parser, executor).whenComplete((unused, throwable) -> {
            this.parseCompleteListener.onComplete(parser.getParsedFacts());
        });
        CompletableFuture.runAsync(exporter, executor).whenComplete((unused, throwable) -> {
            byte[] bos = exporter.writeByte();
            Path tempFile = null;
            try {
                tempFile = Files.createTempFile(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), parser.getFileName());
                Files.write(tempFile, bos);

            } catch (IOException e) {
                LOGGER.error("临时文件写入异常", e);
            }
            if (tempFile != null) {
                ExportFileModel file = new ExportFileModel();
                file.setFileName(exporter.getFileName());
                file.setFilePath(tempFile.toAbsolutePath().toString());
                this.result.put(uuid, file);
            }

        });
        return uuid;
    }

    @Override
    public ExportFileModel getExportFile(String uuid) {
        return this.result.get(uuid);
    }

    @Override
    public void onComplete(ParseCompleteListener callBack) {
        this.parseCompleteListener = callBack;
    }


}
