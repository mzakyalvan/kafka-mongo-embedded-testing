package com.tiket.poc.mongokafkatestingsample.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage.SimpleMessageBuilder;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author zakyalvan
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@JsonDeserialize(builder = SimpleMessageBuilder.class)
@Document(collection = "simple-message")
@SuppressWarnings("serial")
public class SimpleMessage implements Serializable {
  @Id
  private String id;
  private String content;
  private LocalDateTime timestamp;

  @Builder
  public SimpleMessage(String id, String content, LocalDateTime timestamp) {
    this.id = id;
    this.content = content;
    this.timestamp = timestamp;
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class SimpleMessageBuilder {

  }
}
