package com.mindtech.library.controller;

import com.mindtech.library.dto.response.AuthorResponse;
import com.mindtech.library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Author management operations")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(@NotNull final AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "List all authors")
    @NotNull
    public ResponseEntity<Page<AuthorResponse>> findAll(@NotNull final Pageable pageable) {
        return ResponseEntity.ok(this.authorService.findAll(pageable));
    }
}
