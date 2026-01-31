package com.mindtech.library.service;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.exception.custom.DuplicateResourceException;
import com.mindtech.library.exception.custom.ResourceNotFoundException;
import com.mindtech.library.mapper.BookMapper;
import com.mindtech.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BookMapper bookMapper;

    @NotNull
    public PagedResponse<BookResponse> findAll(@NotNull final Pageable pageable) {
        log.debug("Finding all books with pageable: {}", pageable);
        var page = this.bookRepository.findAllWithPublisherAndAuthor(pageable)
                .map(this.bookMapper::toResponse);
        return PagedResponse.from(page);
    }

    @NotNull
    public BookResponse findById(@NotNull final Long id) {
        log.debug("Finding book by id: {}", id);
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return this.bookMapper.toResponse(book);
    }

    @NotNull
    @Transactional
    public BookResponse create(@NotNull final BookRequest request) {
        log.info("Creating new book with ISBN: {}", request.isbn13());
        this.validateIsbn13NotExists(request.isbn13());

        var publisher = this.publisherService.findOrCreate(request.publisherName());
        var book = this.bookMapper.toEntity(request);
        book.setPublisher(publisher);

        var savedBook = this.bookRepository.save(book);
        log.debug("Book saved with id: {}", savedBook.getId());

        var author = this.authorService.create(request.authorNameSurname(), savedBook);
        savedBook.setAuthor(author);

        log.info("Book created successfully with id: {}", savedBook.getId());
        return this.bookMapper.toResponse(savedBook);
    }

    @NotNull
    @Transactional
    public BookResponse update(@NotNull final Long id, @NotNull final BookRequest request) {
        log.info("Updating book with id: {}", id);
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        this.validateIsbn13ForUpdate(request.isbn13(), id);

        this.bookMapper.updateEntity(request, book);

        var publisher = this.publisherService.findOrCreate(request.publisherName());
        book.setPublisher(publisher);

        if (book.getAuthor() != null) {
            book.getAuthor().setNameSurname(request.authorNameSurname());
        }

        var updatedBook = this.bookRepository.save(book);
        log.info("Book updated successfully with id: {}", id);
        return this.bookMapper.toResponse(updatedBook);
    }

    @Transactional
    public void delete(@NotNull final Long id) {
        log.info("Deleting book with id: {}", id);
        var book = this.bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        this.bookRepository.delete(book);
        log.info("Book deleted successfully with id: {}", id);
    }

    @NotNull
    public List<BookResponse> findByTitleStartingWith(@NotNull final String prefix) {
        log.debug("Finding books with title starting with: {}", prefix);
        return this.bookRepository.findAll().stream()
                .filter(book -> book.getTitle().toUpperCase().startsWith(prefix.toUpperCase()))
                .map(this.bookMapper::toResponse)
                .toList();
    }

    @NotNull
    public PagedResponse<BookResponse> findBooksPublishedAfter(
            @NotNull final LocalDate date,
            @NotNull final Pageable pageable
    ) {
        log.debug("Finding books published after: {}", date);
        var page = this.bookRepository.findBooksPublishedAfter(date, pageable)
                .map(this.bookMapper::toResponse);
        return PagedResponse.from(page);
    }

    private void validateIsbn13NotExists(@NotNull final String isbn13) {
        if (this.bookRepository.findByIsbn13(isbn13).isPresent()) {
            log.info("Attempted to create book with existing ISBN: {}", isbn13);
            throw new DuplicateResourceException("Book with ISBN13 " + isbn13 + " already exists");
        }
    }

    private void validateIsbn13ForUpdate(@NotNull final String isbn13, @NotNull final Long bookId) {
        var existingBook = this.bookRepository.findByIsbn13(isbn13);
        if (existingBook.isPresent() && !existingBook.get().getId().equals(bookId)) {
            log.warn("Attempted to update book with existing ISBN: {}", isbn13);
            throw new DuplicateResourceException("Book with ISBN13 " + isbn13 + " already exists");
        }
    }
}