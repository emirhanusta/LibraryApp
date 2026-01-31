package com.mindtech.library.service;

import com.mindtech.library.dto.response.AuthorResponse;
import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.entity.Author;
import com.mindtech.library.entity.Book;
import com.mindtech.library.mapper.AuthorMapper;
import com.mindtech.library.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(
            @NotNull final AuthorRepository authorRepository,
            @NotNull final AuthorMapper authorMapper
    ) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @NotNull
    public PagedResponse<AuthorResponse> findAll(@NotNull final Pageable pageable) {
        log.debug("Finding all authors with pageable: {}", pageable);
        var page = this.authorRepository.findAllWithBook(pageable)
                .map(this.authorMapper::toResponse);
        return PagedResponse.from(page);
    }

    @NotNull
    public Author create(@NotNull final String authorNameSurname, @NotNull final Book book) {
        log.info("Creating author: {} for book: {}", authorNameSurname, book.getTitle());
        var author = new Author(authorNameSurname, book);
        log.info("Author created: {} for book: {}", authorNameSurname, book.getTitle());
        return this.authorRepository.save(author);
    }
}