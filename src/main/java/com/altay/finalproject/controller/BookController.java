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

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> addBook(@RequestBody BookCreateRequest bookCreateRequest) {
        if (!userHasRole("LIBRARIAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only librarians can add books.");
        }
        BookResponse response = bookService.createBook(bookCreateRequest);
        return ResponseEntity.ok(response);
    }

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
    public ResponseEntity<?> getAllBooks() {
        if (!userHasAnyRole("LIBRARIAN", "PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable UUID bookId) {
        if (!userHasRole("PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only patrons can borrow books.");
        }
        try {
            BorrowingRecord borrowingRecord = bookService.borrowBook(getCurrentUser().getId(), bookId);
            return ResponseEntity.ok(borrowingRecord);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable UUID bookId) {
        if (!userHasRole("PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only patrons can return books.");
        }
        try {
            bookService.returnBook(getCurrentUser().getId(), bookId);
            return ResponseEntity.ok("Book returned successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> viewBorrowingHistory() {
        if (!userHasRole("PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only patrons can view their history.");
        }
        List<BorrowingRecord> history = bookService.getBorrowingHistoryForUser(getCurrentUser().getId());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/all-history")
    public ResponseEntity<?> viewAllBorrowingHistory() {
        if (!userHasRole("LIBRARIAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only librarians can view all histories.");
        }
        return ResponseEntity.ok(bookService.getAllBorrowingHistory());
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> manageOverdueBooks() {
        if (!userHasRole("LIBRARIAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only librarians can view overdue books.");
        }
        return ResponseEntity.ok(bookService.getOverdueBooks());
    }


    private AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return bookService.getUserByUsername(username);
    }

    private boolean userHasRole(String role) {
        return getCurrentUser().getRole().name().equalsIgnoreCase(role);
    }

    private boolean userHasAnyRole(String... roles) {
        String currentRole = getCurrentUser().getRole().name();
        for (String role : roles) {
            if (currentRole.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}

