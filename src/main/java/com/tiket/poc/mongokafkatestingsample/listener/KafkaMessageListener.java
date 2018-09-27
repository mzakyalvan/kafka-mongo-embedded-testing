package com.tiket.poc.mongokafkatestingsample.listener;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zakyalvan
 */
@Slf4j
@Component
public class KafkaMessageListener {
  @KafkaListener(topics = "sample.aja.nih")
  public void handleMessage(ConsumerRecord<String, SimpleMessage> messageRecord) {
    log.info("Handle message consumer record : {}", messageRecord);
  }
}
