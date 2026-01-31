package com.mindtech.library.controller;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.dto.response.GoogleBookResponse;
import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.service.BookService;
import com.mindtech.library.service.GoogleBooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management operations")
public class BookController {

    private final BookService bookService;
    private final GoogleBooksService googleBooksService;

    public BookController(
            @NotNull final BookService bookService,
            @NotNull final GoogleBooksService googleBooksService) {
        this.bookService = bookService;
        this.googleBooksService = googleBooksService;
    }

    @GetMapping
    @Operation(summary = "List all books")
    @NotNull
    public ResponseEntity<PagedResponse<BookResponse>> findAll(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(this.bookService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id")
    @NotNull
    public ResponseEntity<BookResponse> findById(@PathVariable @NotNull final Long id) {
        return ResponseEntity.ok(this.bookService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new book with author and publisher")
    @NotNull
    public ResponseEntity<BookResponse> create(@Valid @RequestBody @NotNull final BookRequest request) {
        var created = this.bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book")
    @NotNull
    public ResponseEntity<BookResponse> update(
            @PathVariable @NotNull final Long id,
            @Valid @RequestBody @NotNull final BookRequest request) {
        return ResponseEntity.ok(this.bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    @NotNull
    public ResponseEntity<Void> delete(@PathVariable @NotNull final Long id) {
        this.bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-title-prefix")
    @Operation(summary = "Find books by title prefix using stream")
    @NotNull
    public ResponseEntity<List<BookResponse>> findByTitlePrefix(
            @RequestParam(defaultValue = "A") @NotNull final String prefix) {
        return ResponseEntity.ok(this.bookService.findByTitleStartingWith(prefix));
    }

    @GetMapping("/published-after")
    @Operation(summary = "Find books published after a specific date using JPA query")
    @NotNull
    public ResponseEntity<PagedResponse<BookResponse>> findBooksPublishedAfter(
            @RequestParam(defaultValue = "2026-01-31") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull final LocalDate date,
            @NotNull final Pageable pageable) {
        return ResponseEntity.ok(this.bookService.findBooksPublishedAfter(date, pageable));
    }

    @GetMapping("/search/google")
    @Operation(summary = "Search books from Google Books API")
    @NotNull
    public ResponseEntity<List<GoogleBookResponse>> searchGoogleBooks(
            @RequestParam @NotNull final String query
    ) {
        return ResponseEntity.ok(this.googleBooksService.searchBooks(query));
    }
}