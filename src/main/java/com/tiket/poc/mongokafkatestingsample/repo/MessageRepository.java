package com.tiket.poc.mongokafkatestingsample.repo;

import com.tiket.poc.mongokafkatestingsample.entity.SimpleMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author zakyalvan
 */
public interface MessageRepository extends ReactiveMongoRepository<SimpleMessage, String> {

}
