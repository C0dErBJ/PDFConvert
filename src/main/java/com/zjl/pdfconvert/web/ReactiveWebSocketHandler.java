package com.zjl.pdfconvert.web;

import com.alibaba.fastjson.JSON;
import com.zjl.pdfconvert.executor.AsyncExecutor;
import com.zjl.pdfconvert.web.model.ParseState;
import com.zjl.pdfconvert.web.model.ResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * @author Zhu jialiang
 * @date 2020/9/2
 */
@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final AsyncExecutor executor;

    public ReactiveWebSocketHandler(AsyncExecutor executor) {
        this.executor = executor;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
                session.receive()
                        .map(msg -> {
                            if ("".equals(msg.getPayloadAsText())) {
                                return session.textMessage(JSON.toJSONString(ResponseDto.fail()));
                            }
                            ResponseDto<ParseState> responseDto = ResponseDto.success();
                            if (this.executor.getExportFile(msg.getPayloadAsText()) == null) {
                                responseDto.setData(ParseState.PARSING);
                                return session.textMessage(JSON.toJSONString(responseDto));
                            }
                            responseDto.setData(ParseState.DONE);
                            return session.textMessage(JSON.toJSONString(ResponseDto.success()));
                        }));
    }
}
