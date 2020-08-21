package com.zjl.pdfconvert.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Zhu jialiang
 * @date 2020/8/18
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping()
    public Mono<String> uploadFile(@RequestParam String a) {
        return Mono.just("Hello," + a);
    }
}
