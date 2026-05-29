package com.swapreads.backend.dto;

import com.swapreads.backend.entity.Book;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String condition;
    private String description;
    private Double price;
    private boolean availableForExchange;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;

    private Long ownerId;
    private String ownerName;
    private String ownerEmail;

    public static BookResponse fromEntity(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setIsbn(book.getIsbn());
        response.setGenre(book.getGenre());
        response.setCondition(book.getCondition());
        response.setDescription(book.getDescription());
        response.setPrice(book.getPrice());
        response.setAvailableForExchange(book.isAvailableForExchange());
        response.setImageUrl(book.getImageUrl());
        //response.setStatus(book.getStatus().name());
        response.setStatus(book.getStatus() != null ?
                book.getStatus().name() : "AVAILABLE");
        response.setCreatedAt(book.getCreatedAt());

        if (book.getOwner() != null) {
            response.setOwnerId(book.getOwner().getId());
            response.setOwnerName(book.getOwner().getName());
            response.setOwnerEmail(book.getOwner().getEmail());
        }
        return response;
    }
}
