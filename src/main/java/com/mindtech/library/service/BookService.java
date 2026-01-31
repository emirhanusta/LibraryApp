package com.mindtech.library.service;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.entity.Author;
import com.mindtech.library.entity.Book;
import com.mindtech.library.exception.custom.DuplicateResourceException;
import com.mindtech.library.exception.custom.ResourceNotFoundException;
import com.mindtech.library.repository.AuthorRepository;
import com.mindtech.library.repository.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherService publisherService;

    public BookService(
            @NotNull final BookRepository bookRepository,
            @NotNull final AuthorRepository authorRepository,
            @NotNull final PublisherService publisherService
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherService = publisherService;
    }

    @NotNull
    public Page<BookResponse> findAll(@NotNull final Pageable pageable) {
        return this.bookRepository.findAllWithPublisherAndAuthor(pageable)
                .map(BookResponse::fromEntity);
    }

    @NotNull
    public BookResponse findById(@NotNull final Long id) {
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return BookResponse.fromEntity(book);
    }

    @NotNull
    @Transactional
    public BookResponse create(@NotNull final BookRequest request) {
        this.validateIsbn13NotExists(request.isbn13());

        var publisher = this.publisherService.findOrCreateByName(request.publisherName());
        var book = this.createBookEntity(request, publisher);
        var savedBook = this.bookRepository.save(book);

        var author = new Author(request.authorNameSurname(), savedBook);
        this.authorRepository.save(author);
        savedBook.setAuthor(author);

        return BookResponse.fromEntity(savedBook);
    }

    @NotNull
    @Transactional
    public BookResponse update(@NotNull final Long id, @NotNull final BookRequest request) {
        var book = this.bookRepository.findByIdWithPublisherAndAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        this.validateIsbn13ForUpdate(request.isbn13(), id);
        this.updateBookEntity(book, request);

        return BookResponse.fromEntity(this.bookRepository.save(book));
    }

    @Transactional
    public void delete(@NotNull final Long id) {
        if (!this.bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        this.authorRepository.deleteByBookId(id);
        this.bookRepository.deleteBookById(id);
    }

    @NotNull
    public List<BookResponse> findByTitleStartingWith(@NotNull final String prefix) {
        return this.bookRepository.findAll().stream()
                .filter(book -> book.getTitle().toUpperCase().startsWith(prefix.toUpperCase()))
                .map(BookResponse::fromEntity)
                .toList();
    }

    @NotNull
    public Page<BookResponse> findBooksPublishedAfter(
            @NotNull final LocalDate date,
            @NotNull final Pageable pageable
    ) {
        return this.bookRepository.findBooksPublishedAfter(date, pageable)
                .map(BookResponse::fromEntity);
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

    @NotNull
    private Book createBookEntity(
            @NotNull final BookRequest request,
            @NotNull final com.mindtech.library.entity.Publisher publisher
    ) {
        var book = new Book();
        book.setTitle(request.title());
        book.setPrice(request.price());
        book.setIsbn13(request.isbn13());
        book.setPublisher(publisher);
        book.setPublicationDate(request.publicationDate());
        return book;
    }

    private void updateBookEntity(@NotNull final Book book, @NotNull final BookRequest request) {
        book.setTitle(request.title());
        book.setPrice(request.price());
        book.setIsbn13(request.isbn13());
        book.setPublicationDate(request.publicationDate());

        var publisher = this.publisherService.findOrCreateByName(request.publisherName());
        book.setPublisher(publisher);

        if (book.getAuthor() != null) {
            book.getAuthor().setNameSurname(request.authorNameSurname());
        }
    }
}
