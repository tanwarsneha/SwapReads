package com.swapreads.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_requests")
@Data
public class ExchangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_book_id", nullable = false)
    private Book requestedBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offered_book_id")
    private Book offeredBook;

    @Enumerated(EnumType.STRING)
    private ExchangeStatus status = ExchangeStatus.PENDING;

    private String message;
    private String responseMessage;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime respondedAt;

    public enum ExchangeStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED,
        COMPLETED
    }
}
