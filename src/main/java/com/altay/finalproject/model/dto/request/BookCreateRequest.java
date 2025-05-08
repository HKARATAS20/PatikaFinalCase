package com.altay.finalproject.model.dto.request;


import lombok.Data;

import java.time.LocalDate;

@Data
public class BookCreateRequest {
    private String title;
    private String author;
    private String isbn;
    private String genre;
}