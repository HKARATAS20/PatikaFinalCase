package com.altay.finalproject.model.dto.response;

import com.altay.finalproject.model.entity.Book;

import java.util.UUID;

public record BookResponse (
        UUID id,
        String title,
        String author,
        String isbn,
        Book.Genre genre,
        Boolean isAvailable
)
{}
