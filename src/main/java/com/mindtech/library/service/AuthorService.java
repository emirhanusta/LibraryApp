package com.mindtech.library.service;

import com.mindtech.library.dto.response.AuthorResponse;
import com.mindtech.library.repository.AuthorRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(@NotNull final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @NotNull
    public Page<AuthorResponse> findAll(@NotNull final Pageable pageable) {
        return this.authorRepository.findAllWithBook(pageable)
                .map(AuthorResponse::fromEntity);
    }
}
