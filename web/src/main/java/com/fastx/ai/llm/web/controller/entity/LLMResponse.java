package com.fastx.ai.llm.web.controller.entity;

import org.apache.dubbo.common.stream.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author stark
 */
public class LLMResponse implements StreamingResponseBody {

    Logger log = LoggerFactory.getLogger(LLMResponse.class);

    private StreamObserver<String> streamObserver;

    private OutputStream os;

    private OutputStreamWriter writer;

    private CountDownLatch countDownLatch;

    public LLMResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;

        this.streamObserver = new StreamObserver<String>() {
            @Override
            public void onNext(String data) {
                try {
                    writer.write(data.toCharArray());
                    writer.flush();
                } catch (IOException e) {
                    log.error("write stream to front meet error", e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("dubbo stream meet error", throwable);
                try {
                    writer.close();
                    os.close();
                } catch (IOException e) {
                    log.error("close stream meet error", e);
                }
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                try {
                    writer.close();
                    os.close();
                } catch (IOException e) {
                    log.error("dubbo complete, close stream meet error", e);
                }
                countDownLatch.countDown();
            }
        };
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        this.os = outputStream;
        this.writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public StreamObserver<String> getStreamObserver() {
        return streamObserver;
    }
}
