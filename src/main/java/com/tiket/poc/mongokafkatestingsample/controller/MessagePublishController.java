package com.tiket.poc.mongokafkatestingsample.controller;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import com.tiket.poc.mongokafkatestingsample.service.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author zakyalvan
 */
@Slf4j
@RestController
@RequestMapping("/messages")
public class MessagePublishController {
    @Autowired
    private MessagePublisher publisher;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<SimpleMessage> publish(@RequestParam(name = "c") String content) {
        return publisher.publish(content)
                .log()
                .filter(message -> message.getContent().equals(content))
                .single()
                .subscribeOn(Schedulers.elastic());
    }
}
