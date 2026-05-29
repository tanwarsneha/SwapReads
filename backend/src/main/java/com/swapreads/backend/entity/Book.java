package com.swapreads.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String isbn;
    private String genre;
    @Column(name = "book_condition")
    private String condition;

    private String description;
    private Double price;
    private boolean availableForExchange = true;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BookStatus {
        AVAILABLE, EXCHANGED, SOLD, RESERVED
    }
}