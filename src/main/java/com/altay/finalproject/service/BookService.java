package com.altay.finalproject.service;

import com.altay.finalproject.model.dto.request.BookCreateRequest;
import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.AppUser;
import com.altay.finalproject.model.entity.Book;
import com.altay.finalproject.model.entity.BorrowingRecord;
import com.altay.finalproject.model.mapper.BookMapper;
import com.altay.finalproject.model.mapper.BookResponseMapper;
import com.altay.finalproject.repository.AppUserRepository;
import com.altay.finalproject.repository.BookRepository;
import com.altay.finalproject.repository.BorrowingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookService {

    Map<String, Book> books = new HashMap<>();

    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

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

    public BorrowingRecord borrowBook(UUID userId, UUID bookId) {
        // Ensure book is available
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        // Ensure user is eligible (you can add checks like user account is active, etc.)

        // Create the BorrowingRecord
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setUser(appUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        borrowingRecord.setBook(book);
        borrowingRecord.setBorrowedDate(LocalDate.now());
        borrowingRecord.setDueDate(LocalDate.now().plusDays(14)); // Example due date
        borrowingRecord.setStatus(BorrowingRecord.BorrowingStatus.BORROWED);

        // Save the borrowing record
        borrowingRecordRepository.save(borrowingRecord);

        // Update the book availability
        book.setAvailable(false);
        bookRepository.save(book);

        return borrowingRecord;
    }

    public void returnBook(UUID userId, UUID bookId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository
                .findByUser_Id(userId)
                .stream()
                .filter(record -> record.getBook().getId().equals(bookId) && record.getStatus() == BorrowingRecord.BorrowingStatus.BORROWED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found or not borrowed"));

        // Update the borrowing record status to "RETURNED"
        borrowingRecord.setReturnedDate(LocalDate.now());
        borrowingRecord.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);

        // Update the book availability
        Book book = borrowingRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        borrowingRecordRepository.save(borrowingRecord);
    }

    public List<BorrowingRecord> getBorrowingHistoryForUser(UUID userId) {
        return borrowingRecordRepository.findByUser_Id(userId);
    }

    public List<BorrowingRecord> getAllBorrowingHistory() {
        return borrowingRecordRepository.findAll();
    }

    public List<BorrowingRecord> getOverdueBooks() {
        return borrowingRecordRepository.findAll().stream()
                .filter(record -> record.getDueDate().isBefore(LocalDate.now()) && record.getStatus() != BorrowingRecord.BorrowingStatus.RETURNED)
                .collect(Collectors.toList());
    }

    public AppUser getUserByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }








}