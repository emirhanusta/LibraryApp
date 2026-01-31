package com.mindtech.library.service;

import com.mindtech.library.dto.response.AuthorResponse;
import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.mapper.AuthorMapper;
import com.mindtech.library.repository.AuthorRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        var page = this.authorRepository.findAllWithBook(pageable)
                .map(this.authorMapper::toResponse);
        return PagedResponse.from(page);
    }
}