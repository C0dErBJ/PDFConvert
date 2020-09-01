package com.zjl.pdfconvert.config;

import com.zjl.pdfconvert.exporter.Executor;
import com.zjl.pdfconvert.exporter.SyncPdfExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Zhu jialiang
 * @date 2020/8/31
 */
@Configuration
public class PdfConfiguration {

    @Bean
    public Executor executor() {
        return new SyncPdfExecutor();
    }

}
