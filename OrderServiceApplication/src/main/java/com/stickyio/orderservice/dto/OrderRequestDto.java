package com.stickyio.orderservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {
    Long customerId;
    String item;

    public OrderRequestDto(Long customerId, String item) {
        this.customerId = customerId;
        this.item = item;
    }

    @Override
    public String toString() {
        return "OrderRequestDto{" +
                "customerId=" + customerId +
                ", item='" + item + '\'' +
                '}';
    }
}