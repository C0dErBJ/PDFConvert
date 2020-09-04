package com.zjl.pdfconvert.config;

import com.zjl.pdfconvert.executor.AsyncExecutor;
import com.zjl.pdfconvert.executor.AsyncPdfExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhu jialiang
 * @date 2020/8/31
 */
@Configuration
public class PdfConfiguration {
    @Autowired
    private  WebSocketHandler webSocketHandler;


    @Bean
    public AsyncExecutor executor() {
        return new AsyncPdfExecutor();
    }


    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>(1);
        map.put("/parserState", webSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
