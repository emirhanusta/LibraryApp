package com.mindtech.library.service;

import com.mindtech.library.dto.response.PublisherResponse;
import com.mindtech.library.dto.response.PublisherWithBooksResponse;
import com.mindtech.library.entity.Publisher;
import com.mindtech.library.repository.PublisherRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(@NotNull final PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @NotNull
    public Page<PublisherResponse> findAll(@NotNull final Pageable pageable) {
        return this.publisherRepository.findAll(pageable)
                .map(PublisherResponse::fromEntity);
    }

    @NotNull
    public Page<PublisherWithBooksResponse> findPublishersWithBooksAndAuthors(
            final int count,
            @NotNull final Pageable pageable
    ) {
        var idPage = this.publisherRepository.findAllPublisherIds(Pageable.ofSize(count));
        var ids = idPage.getContent();

        return this.publisherRepository.findPublishersWithBooksAndAuthors(ids, pageable)
                .map(PublisherWithBooksResponse::fromEntity);
    }

    @NotNull
    public Publisher findOrCreateByName(@NotNull final String name) {
        return this.publisherRepository.findByName(name)
                .orElseGet(() -> {
                    var publisher = new Publisher(name);
                    return this.publisherRepository.save(publisher);
                });
    }
}
