/* * Copyright 2023-2024 the original author or authors. * * TBD */

package com.stickyio.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingResponseDto {

  private Long orderId;
  private String currentStatus;
  private Boolean isDelivered;
  private Date responseTimestamp;

  @Override
  public String toString() {
    return "TrackingResponseDto{" +
        "orderId=" + orderId +
        ", currentStatus='" + currentStatus + '\'' +
        ", isDelivered=" + isDelivered +
        ", responseTimestamp=" + responseTimestamp +
        '}';
  }
}
