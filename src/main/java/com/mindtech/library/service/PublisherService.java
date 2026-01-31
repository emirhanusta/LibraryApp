package com.mindtech.library.service;

import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.dto.response.PublisherResponse;
import com.mindtech.library.dto.response.PublisherWithBooksResponse;
import com.mindtech.library.entity.Publisher;
import com.mindtech.library.mapper.PublisherMapper;
import com.mindtech.library.repository.PublisherRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(
            @NotNull final PublisherRepository publisherRepository,
            @NotNull final PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @NotNull
    public PagedResponse<PublisherResponse> findAll(@NotNull final Pageable pageable) {
        var page = this.publisherRepository.findAll(pageable)
                .map(this.publisherMapper::toResponse);
        return PagedResponse.from(page);
    }

    @NotNull
    public PagedResponse<PublisherWithBooksResponse> findPublishersWithBooksAndAuthors(
            final int count,
            @NotNull final Pageable pageable) {
        var idPage = this.publisherRepository.findAllPublisherIds(Pageable.ofSize(count));
        var ids = idPage.getContent();

        var page = this.publisherRepository.findPublishersWithBooksAndAuthors(ids, pageable)
                .map(this.publisherMapper::toResponseWithBooks);
        return PagedResponse.from(page);
    }

    @NotNull
    public Optional<Publisher> findByName(@NotNull final String name) {
        return this.publisherRepository.findByName(name);
    }

    @NotNull
    @Transactional
    public Publisher create(@NotNull final String name) {
        var publisher = new Publisher(name);
        return this.publisherRepository.save(publisher);
    }

    @NotNull
    @Transactional
    public Publisher findOrCreate(@NotNull final String name) {
        return this.findByName(name)
                .orElseGet(() -> this.create(name));
    }
}