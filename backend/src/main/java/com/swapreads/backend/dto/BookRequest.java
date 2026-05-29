package com.swapreads.backend.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String condition;
    private String description;
    private Double price;
    private boolean availableForExchange = true;
    private String imageUrl;
}
