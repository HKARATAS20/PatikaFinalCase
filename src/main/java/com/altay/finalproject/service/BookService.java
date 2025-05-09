package com.altay.finalproject.service;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.Book;
import com.altay.finalproject.model.mapper.BookMapper;
import com.altay.finalproject.model.mapper.BookResponseMapper;
import com.altay.finalproject.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookService {

    Map<String, Book> books = new HashMap<>();

    private final BookRepository bookRepository;

    // Event publishing for task operations (Java 8 functional interfaces)
    //private final List<Consumer<Task>> taskCreationListeners = new ArrayList<>();
    //private final List<Consumer<Task>> taskCompletionListeners = new ArrayList<>();

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        //validateTaskInput(title, description);

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(Book.Genre.valueOf(request.getGenre().toUpperCase()))
                .build();

        Book savedBook = bookRepository.save(book);

        // Notify creation listeners
        //taskCreationListeners.forEach(listener -> listener.accept(savedTask));

        return BookMapper.toDTO(savedBook);
    }

    public Book getBook(String id) {
        return books.get(id);
    }
    public List<BookResponse> getAllBooks() {
        return BookResponseMapper.INSTANCE.toDTOList(bookRepository.findAll());
    }



}