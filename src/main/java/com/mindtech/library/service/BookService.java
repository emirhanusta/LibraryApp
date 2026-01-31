package com.mindtech.library.service;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.dto.response.PagedResponse;
import com.mindtech.library.exception.custom.DuplicateResourceException;
import com.mindtech.library.exception.custom.ResourceNotFoundException;
import com.mindtech.library.mapper.BookMapper;
import com.mindtech.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
        var page = this.bookRepository.findAllWithPublisherAndAuthor(pageable)
                .map(this.bookMapper::toResponse);
        return PagedResponse.from(page);
    }

    @NotNull
    public BookResponse findById(@NotNull final Long id) {
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return this.bookMapper.toResponse(book);
    }

    @NotNull
    @Transactional
    public BookResponse create(@NotNull final BookRequest request) {
        this.validateIsbn13NotExists(request.isbn13());

        var publisher = this.publisherService.findOrCreate(request.publisherName());
        var book = this.bookMapper.toEntity(request);
        book.setPublisher(publisher);

        var savedBook = this.bookRepository.save(book);

        var author = this.authorService.create(request.authorNameSurname(), savedBook);

        savedBook.setAuthor(author);

        return this.bookMapper.toResponse(savedBook);
    }

    @NotNull
    @Transactional
    public BookResponse update(@NotNull final Long id, @NotNull final BookRequest request) {
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        this.validateIsbn13ForUpdate(request.isbn13(), id);

        this.bookMapper.updateEntity(request, book);

        var publisher = this.publisherService.findOrCreate(request.publisherName());
        book.setPublisher(publisher);

        if (book.getAuthor() != null) {
            book.getAuthor().setNameSurname(request.authorNameSurname());
        }

        return this.bookMapper.toResponse(this.bookRepository.save(book));
    }

    @Transactional
    public void delete(@NotNull final Long id) {
        if (!this.bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        this.authorService.deleteByBookId(id);
        this.bookRepository.deleteBookById(id);
    }

    @NotNull
    public List<BookResponse> findByTitleStartingWith(@NotNull final String prefix) {
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
        var page = this.bookRepository.findBooksPublishedAfter(date, pageable)
                .map(this.bookMapper::toResponse);
        return PagedResponse.from(page);
    }

    private void validateIsbn13NotExists(@NotNull final String isbn13) {
        if (this.bookRepository.findByIsbn13(isbn13).isPresent()) {
            throw new DuplicateResourceException("Book with ISBN13 " + isbn13 + " already exists");
        }
    }

    private void validateIsbn13ForUpdate(@NotNull final String isbn13, @NotNull final Long bookId) {
        var existingBook = this.bookRepository.findByIsbn13(isbn13);
        if (existingBook.isPresent() && !existingBook.get().getId().equals(bookId)) {
            throw new DuplicateResourceException("Book with ISBN13 " + isbn13 + " already exists");
        }
    }
}