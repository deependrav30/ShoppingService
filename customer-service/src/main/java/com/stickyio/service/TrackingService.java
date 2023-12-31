/* * Copyright 2023-2024 the original author or authors. * * TBD */

package com.stickyio.service;

import static com.stickyio.util.CustomerConstants.TRACK_ORDER_REPLY_TOPIC;
import static com.stickyio.util.CustomerConstants.TRACK_ORDER_REQUEST_TOPIC;

import com.stickyio.dao.CustomerOrderMapping;
import com.stickyio.dto.TrackingRequestDto;
import com.stickyio.dto.TrackingResponseDto;
import com.stickyio.repository.CustomerOrderRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrackingService {

  @Autowired
  CustomerOrderRepository customerOrderRepository;
  @Autowired
  ReplyingKafkaTemplate<String, TrackingRequestDto, TrackingResponseDto> kafkaTemplateForTracking;

  @Transactional
  public CustomerOrderMapping createTrackingRequest(Long orderId)
      throws InterruptedException, ExecutionException, TimeoutException {
    log.info(String.format("Tracking for: %s", orderId));
    if (customerOrderRepository.findFirstByOrderId(orderId).isEmpty()) {
      return new CustomerOrderMapping(null, orderId, null, "Order Not Found", new Date());
    }
    if (!kafkaTemplateForTracking.waitForAssignment(Duration.ofSeconds(10))) {
      throw new IllegalStateException("Reply container did not initialize");
    }
    ProducerRecord<String, TrackingRequestDto> record = new ProducerRecord<>(
        TRACK_ORDER_REQUEST_TOPIC, new TrackingRequestDto(orderId));
    record.headers().add(new RecordHeader(
        KafkaHeaders.REPLY_TOPIC, TRACK_ORDER_REPLY_TOPIC.getBytes()));
    RequestReplyFuture<String, TrackingRequestDto, TrackingResponseDto> sendAndReceive =
        kafkaTemplateForTracking.sendAndReceive(record, Duration.ofSeconds(10));
    SendResult<String, TrackingRequestDto> sendResult = sendAndReceive.getSendFuture().get();
    ConsumerRecord<String, TrackingResponseDto> consumerRecord = sendAndReceive.get();
    log.info(String.format("Received Tracking Response: %s", consumerRecord.value().toString()));
    customerOrderRepository.updateCourierStatusByOrderIdAndDate(
        consumerRecord.value().getOrderId(),
        consumerRecord.value().getCurrentStatus(),
        consumerRecord.value().getResponseTimestamp()
    );
    return customerOrderRepository.findFirstByOrderId(orderId).get();
  }
}
