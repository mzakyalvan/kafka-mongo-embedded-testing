# README

Maaf, readme nya belum sempat ditulis ya. Yang penting sih liat test class (Cuman satu).

```java

package com.tiket.poc.mongokafkatestingsample;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import com.tiket.poc.mongokafkatestingsample.listener.KafkaMessageListener;
import com.tiket.poc.mongokafkatestingsample.listener.KafkaSampleConfiguration;
import com.tiket.poc.mongokafkatestingsample.repo.MessageRepository;
import com.tiket.poc.mongokafkatestingsample.service.MessagePublisher;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import static org.springframework.kafka.test.utils.ContainerTestUtils.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author zakyalvan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApplication.class,
    properties = {
      "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
    })
@EmbeddedKafka(topics = "sample.aja.nih",
    partitions = KafkaSampleConfiguration.TOPIC_PARTITIONS)
public class SampleApplicationTests {

  @Autowired
  private KafkaListenerEndpointRegistry listenerRegistry;

  @Autowired
  private MessagePublisher messagePublisher;

  @Autowired
  private MessageRepository messageRepository;

  @SpyBean
  private KafkaMessageListener messageListener;

  @Before
  public void prepareTests() throws Exception {
    for(MessageListenerContainer container : listenerRegistry.getListenerContainers()) {
      waitForAssignment(container, KafkaSampleConfiguration.TOPIC_PARTITIONS);
    }
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void whenPublishMessage_thenListenerReceiveMessage() throws Exception {
    String content = "Santai dikit pak!";

    StepVerifier.create(messagePublisher.publish(content))
        .expectSubscription()
        .thenAwait()
        .assertNext(message -> message.getContent().equalsIgnoreCase(content))
        .verifyComplete();

    // Wait a moment...
    Thread.sleep(500);

    ArgumentCaptor<ConsumerRecord<String, SimpleMessage>> captor = ArgumentCaptor.forClass(ConsumerRecord.class);

    verify(messageListener, times(1))
            .handleMessage(captor.capture());

    SimpleMessage listenedMessage = captor.getValue().value();

    assertThat(listenedMessage.getContent(), equalTo(content));
    assertThat(messageRepository.existsById(listenedMessage.getId()).block(), is(true));
  }
}

```

Happy Coding...
