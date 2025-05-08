package com.altay.finalproject.controller;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.entity.Book;
import com.altay.finalproject.service.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {


    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /*
    {
    "title": "Eclipse",
    "author": "Andy Weirin Kardeşi",
    "isbn": "12345678",
    "genre": "FICTION"
    }
     */

    @PostMapping(produces = "application/json",consumes = "application/json")
    public Book addBook(@RequestBody BookCreateRequest bookCreateRequest) {
        System.out.println("request is " + bookCreateRequest);
        return bookService.createBook(bookCreateRequest);
    }
}
