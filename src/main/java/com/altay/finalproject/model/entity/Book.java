package com.altay.finalproject.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "genre", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    // Lifecycle methods for setting timestamps and default ID

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (publicationDate == null) {
            publicationDate = LocalDate.now();
        }
        isAvailable = true; // default to available

    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        this.isAvailable = available;
    }



    public enum Genre {
        FICTION,
        NON_FICTION,
        SCIENCE,
        HISTORY,
        BIOGRAPHY,
        FANTASY,
        MYSTERY,
        ROMANCE,
        OTHER
    }

    // Builder default values
    public static class BookBuilder {
        private UUID id = java.util.UUID.randomUUID();
        private LocalDate publicationDate = LocalDate.now();
        private boolean isAvailable = true;
    }
}

