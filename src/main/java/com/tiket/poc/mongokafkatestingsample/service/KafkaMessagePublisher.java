package com.tiket.poc.mongokafkatestingsample.service;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import com.tiket.poc.mongokafkatestingsample.repo.MessageRepository;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.retry.Retry;

/**
 * @author zakyalvan
 */
@Service
public class KafkaMessagePublisher implements MessagePublisher, InitializingBean {
//  private Flux<SimpleMessage> messageProcessor;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private KafkaTemplate<String, SimpleMessage> kafkaTemplate;

  @Override
  public Mono<SimpleMessage> publish(String messageContent) {
    return Mono.fromCallable(() -> messageContent)
        .map(content -> SimpleMessage
            .builder()
            .content(content).timestamp(LocalDateTime.now()).build())
            .flatMap(message -> messageRepository.save(message)
                    .retryWhen(Retry.any().fixedBackoff(Duration.ofMillis(250)).retryMax(3))
                    .subscribeOn(Schedulers.elastic())
            )
            .flatMap(message -> Mono.<SendResult>create(sink -> kafkaTemplate.sendDefault(message).addCallback(result -> sink.success(result), ex -> sink.error(ex)))
                    .retryWhen(Retry.any().fixedBackoff(Duration.ofMillis(250)).retryMax(3))
                    .map(result -> message)
                    .log()
                    .subscribeOn(Schedulers.elastic())
            );
  }

  @Override
  public void afterPropertiesSet() throws Exception {
//    messageProcessor = DirectProcessor.<String>create()
//        .onBackpressureBuffer(1_000, BufferOverflowStrategy.DROP_LATEST)
//        .map(content -> SimpleMessage
//                .builder()
//                .content(content).timestamp(LocalDateTime.now()).build())
//        .flatMap(message -> messageRepository.save(message)
//            .retryWhen(Retry.any().fixedBackoff(Duration.ofMillis(250)).retryMax(3))
//            .subscribeOn(Schedulers.elastic())
//        )
//        .flatMap(message -> Mono.<SendResult>create(sink -> kafkaTemplate.sendDefault(message).addCallback(result -> sink.success(result), ex -> sink.error(ex)))
//            .retryWhen(Retry.any().fixedBackoff(Duration.ofMillis(250)).retryMax(3))
//            .map(result -> message)
//            .log()
//            .subscribeOn(Schedulers.elastic())
//        ).share();
  }
}
