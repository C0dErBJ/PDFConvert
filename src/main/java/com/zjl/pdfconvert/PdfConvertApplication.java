package com.zjl.pdfconvert;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;


/**
 * @author Zhu jialiang
 */
@SpringBootApplication
public class PdfConvertApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfConvertApplication.class, args);

    }

}
