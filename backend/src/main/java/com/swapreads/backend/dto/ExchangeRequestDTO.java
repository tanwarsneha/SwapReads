package com.swapreads.backend.dto;

import lombok.Data;

@Data
public class ExchangeRequestDTO {
    private Long requestedBookId;
    private Long offeredBookId;
    private String message;
}
