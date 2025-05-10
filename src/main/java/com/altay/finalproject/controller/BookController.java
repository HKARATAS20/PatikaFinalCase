package com.altay.finalproject.controller;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.AppUser;
import com.altay.finalproject.model.entity.BorrowingRecord;
import com.altay.finalproject.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
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


    @Operation(
            summary = "Add a new book",
            description = "Adds a new book given correct information. Can only be accessed with a user of role librarian",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book added successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class))))
            }
    )
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
            description = "Retrieves a list of all books in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of books retrieved successfully",
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


    @Operation(
            summary = "Borrow a book",
            description = "Allows a user with the role of PATRON to borrow a book by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book borrowed successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BorrowingRecord.class))),
                    @ApiResponse(responseCode = "403", description = "Only patrons can borrow books"),
                    @ApiResponse(responseCode = "400", description = "Book cannot be borrowed (e.g., unavailable)")
            }
    )
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

    @Operation(
            summary = "Return a book",
            description = "Allows a user with the role of PATRON to return a borrowed book by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book returned successfully"),
                    @ApiResponse(responseCode = "403", description = "Only patrons can return books"),
                    @ApiResponse(responseCode = "400", description = "Book return failed (e.g., not borrowed)")
            }
    )
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


    @Operation(
            summary = "View personal borrowing history",
            description = "Retrieves the borrowing history for the currently authenticated patron",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Borrowing history retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BorrowingRecord.class)))),
                    @ApiResponse(responseCode = "403", description = "Only patrons can view their history")
            }
    )
    @GetMapping("/history")
    public ResponseEntity<?> viewBorrowingHistory() {
        if (!userHasRole("PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only patrons can view their history.");
        }
        List<BorrowingRecord> history = bookService.getBorrowingHistoryForUser(getCurrentUser().getId());
        return ResponseEntity.ok(history);
    }

    @Operation(
            summary = "View all borrowing histories",
            description = "Allows a LIBRARIAN to view the borrowing history of all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All borrowing histories retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BorrowingRecord.class)))),
                    @ApiResponse(responseCode = "403", description = "Only librarians can view all histories")
            }
    )
    @GetMapping("/all-history")
    public ResponseEntity<?> viewAllBorrowingHistory() {
        if (!userHasRole("LIBRARIAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only librarians can view all histories.");
        }
        return ResponseEntity.ok(bookService.getAllBorrowingHistory());
    }

    @Operation(
            summary = "View overdue books",
            description = "Allows a LIBRARIAN to retrieve a list of overdue books",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Overdue books retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class)))),
                    @ApiResponse(responseCode = "403", description = "Only librarians can view overdue books")
            }
    )
    @GetMapping("/overdue")
    public ResponseEntity<?> manageOverdueBooks() {
        if (!userHasRole("LIBRARIAN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only librarians can view overdue books.");
        }
        return ResponseEntity.ok(bookService.getOverdueBooks());
    }

    @Operation(
            summary = "Get book details",
            description = "Fetches the details of a specific book using its ID. Accessible to both librarians and patrons.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book details retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookDetails(@PathVariable UUID bookId) {
        if (!userHasAnyRole("LIBRARIAN", "PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        try {
            return ResponseEntity.ok(bookService.getBookDetails(bookId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
    }

    @Operation(
            summary = "Search books by title",
            description = "Searches for books that match the given title. Accessible to librarians and patrons.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matching books retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookResponse.class)))),
                    @ApiResponse(responseCode = "403", description = "Access denied")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String title) {
        if (!userHasAnyRole("LIBRARIAN", "PATRON")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
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

