package com.altay.finalproject.controller;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.Book;
import com.altay.finalproject.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BookResponse addBook(@RequestBody BookCreateRequest bookCreateRequest) {
        System.out.println("request is " + bookCreateRequest);
        return bookService.createBook(bookCreateRequest);
    }

  /*  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    // HTTP Post
    // HTTP Status Code : 201
    // HTTP Body : JSON
    // HTTP Response : JSON

    // @RequestBody : It is a Spring annotation that tells Spring to automatically deserialize the request body into the TaskRequest object.

    @Operation(
            summary = "Create a new book",
            description = "Creates a new task with the provided title, author, isbn and genre",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<BookResponse> createTask(@RequestBody BookCreateRequest request) {
        BookResponse newBookResponse =  bookService.createBook(request);
        return new ResponseEntity<>(newBookResponse, HttpStatus.CREATED);
    }
*/
    // CRUD Operations
    // Create, Read, Update, Delete
    @Operation(
            summary = "Get all books",
            description = "Retrieves a list of all tasks in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of tasks retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
            }
    )
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }



}
