package com.swapreads.backend.dto;

import com.swapreads.backend.entity.ExchangeRequest;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExchangeResponseDTO {
    private Long id;
    private String status;
    private String message;
    private String responseMessage;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

    private Long requesterId;
    private String requesterName;
    private String requesterEmail;

    private Long ownerId;
    private String ownerName;

    private Long requestedBookId;
    private String requestedBookTitle;

    private Long offeredBookId;
    private String offeredBookTitle;

    public static ExchangeResponseDTO fromEntity(ExchangeRequest req) {
        ExchangeResponseDTO dto = new ExchangeResponseDTO();
        dto.setId(req.getId());
        dto.setStatus(req.getStatus().name());
        dto.setMessage(req.getMessage());
        dto.setResponseMessage(req.getResponseMessage());
        dto.setCreatedAt(req.getCreatedAt());
        dto.setRespondedAt(req.getRespondedAt());

        dto.setRequesterId(req.getRequester().getId());
        dto.setRequesterName(req.getRequester().getName());
        dto.setRequesterEmail(req.getRequester().getEmail());

        dto.setOwnerId(req.getOwner().getId());
        dto.setOwnerName(req.getOwner().getName());

        dto.setRequestedBookId(req.getRequestedBook().getId());
        dto.setRequestedBookTitle(req.getRequestedBook().getTitle());

        if (req.getOfferedBook() != null) {
            dto.setOfferedBookId(req.getOfferedBook().getId());
            dto.setOfferedBookTitle(req.getOfferedBook().getTitle());
        }

        return dto;
    }
}
