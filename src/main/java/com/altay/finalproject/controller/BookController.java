package com.altay.finalproject.controller;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.AppUser;
import com.altay.finalproject.model.entity.Book;
import com.altay.finalproject.model.entity.BorrowingRecord;
import com.altay.finalproject.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable UUID bookId) {


        try {
            BorrowingRecord borrowingRecord = bookService.borrowBook(getCurrentUserId(), bookId);
            return ResponseEntity.ok(borrowingRecord);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/return/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable UUID bookId) {

        try {
            bookService.returnBook(getCurrentUserId(), bookId);
            return ResponseEntity.ok("Book returned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<BorrowingRecord>> viewBorrowingHistory() {

        List<BorrowingRecord> history = bookService.getBorrowingHistoryForUser(getCurrentUserId());
        return ResponseEntity.ok(history);
    }
    @GetMapping("/all-history")
    public ResponseEntity<List<BorrowingRecord>> viewAllBorrowingHistory() {
        List<BorrowingRecord> history = bookService.getAllBorrowingHistory();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowingRecord>> manageOverdueBooks() {
        List<BorrowingRecord> overdueBooks = bookService.getOverdueBooks();
        return ResponseEntity.ok(overdueBooks);
    }



    private UUID getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = bookService.getUserByUsername(username);
        return user.getId();
    }





}
