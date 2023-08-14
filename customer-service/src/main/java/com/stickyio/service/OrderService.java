/* * Copyright 2023-2024 the original author or authors. * * TBD */

package com.stickyio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.stickyio.dao.CustomerOrderMapping;
import com.stickyio.repository.CustomerOrderRepository;
import com.stickyio.dto.OrderReplyDto;
import com.stickyio.dto.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {
    @Autowired
    CustomerOrderRepository customerOrderRepository;

    @Autowired
    KafkaTemplate<String, OrderRequestDto> kafkaTemplate;

    public void createOrder(OrderRequestDto order){
        createOrderRequest(order);
    }
    public void createOrderRequest(OrderRequestDto order){
        log.info(String.format("Create Order Request: %s",order.toString()));

        Message<OrderRequestDto> message = MessageBuilder
                .withPayload(order)
                .setHeader(KafkaHeaders.TOPIC,"create-order")
                .build();

        kafkaTemplate.send(message);
    }

    @KafkaListener(topics = "create-order-reply", groupId = "shoppingGroup")
    void getOrderReply(OrderReplyDto orderReply)
    {
        log.info(String.format("Order created with order-id: %s",orderReply.getOrderId()));
        customerOrderRepository.save(new CustomerOrderMapping(
                orderReply.getCustomerId(), orderReply.getOrderId())
        );
    }
}