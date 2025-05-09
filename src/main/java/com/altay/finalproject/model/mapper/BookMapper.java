package com.altay.finalproject.model.mapper;

import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.Book;

public class BookMapper {


    private BookMapper() {}

    public static BookResponse toDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getGenre()
        );
    }

}

