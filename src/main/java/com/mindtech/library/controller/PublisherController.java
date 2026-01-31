package com.mindtech.library.controller;

import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.dto.response.PublisherResponse;
import com.mindtech.library.dto.response.PublisherWithBooksResponse;
import com.mindtech.library.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publishers", description = "Publisher management operations")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(@NotNull final PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    @Operation(summary = "List all publishers")
    @NotNull
    public ResponseEntity<PagedResponse<PublisherResponse>> findAll(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(this.publisherService.findAll(pageable));
    }

    @GetMapping("/with-books")
    @Operation(summary = "List publishers with their books and authors")
    @NotNull
    public ResponseEntity<PagedResponse<PublisherWithBooksResponse>> findPublishersWithBooks(
            @RequestParam(defaultValue = "2") final int count,
            @NotNull final Pageable pageable
    ) {
        return ResponseEntity.ok(this.publisherService.findPublishersWithBooksAndAuthors(count, pageable));
    }
}