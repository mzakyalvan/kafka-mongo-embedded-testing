package com.tiket.poc.mongokafkatestingsample.service;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author zakyalvan
 */
public interface MessagePublisher {
  Mono<SimpleMessage> publish(String messageContent);
}
