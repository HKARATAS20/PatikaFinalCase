package com.altay.finalproject.service;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.entity.Book;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    Map<String, Book> books = new HashMap<>();

    public Book createBook(BookCreateRequest bookRequest) {
        Book book = new Book();

        UUID id = UUID.randomUUID();
        book.setId(id);
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublicationDate(LocalDate.now());
        book.setGenre(Book.Genre.valueOf(bookRequest.getGenre()));

        books.put(id.toString(), book);
        return book;
    }

    public Map<String, Book> getBooks() {
        return books;
    }

    public Book getBook(String id) {
        return books.get(id);
    }
}