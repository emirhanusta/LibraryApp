package com.mindtech.library.service;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.entity.Author;
import com.mindtech.library.entity.Book;
import com.mindtech.library.entity.Publisher;
import com.mindtech.library.exception.custom.DuplicateResourceException;
import com.mindtech.library.exception.custom.ResourceNotFoundException;
import com.mindtech.library.mapper.BookMapper;
import com.mindtech.library.repository.AuthorRepository;
import com.mindtech.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Publisher testPublisher;
    private Book testBook;
    private BookRequest testRequest;
    private BookResponse testResponse;

    @BeforeEach
    void setUp() {
        this.testPublisher = new Publisher("Test Publisher");
        this.testPublisher.setId(1L);

        this.testBook = new Book();
        this.testBook.setId(1L);
        this.testBook.setTitle("Test Book");
        this.testBook.setPrice(BigDecimal.valueOf(29.99));
        this.testBook.setIsbn13("9780134685991");
        this.testBook.setPublisher(this.testPublisher);

        var author = new Author("Test Author", this.testBook);
        author.setId(1L);
        this.testBook.setAuthor(author);

        this.testRequest = new BookRequest(
                "New Book",
                BigDecimal.valueOf(39.99),
                "9780134686042",
                "Test Publisher",
                "New Author",
                null
        );

        this.testResponse = new BookResponse(
                2L,
                "New Book",
                BigDecimal.valueOf(39.99),
                "9780134686042",
                "Test Publisher",
                "New Author",
                null
        );
    }

    @Test
    @DisplayName("Should create book successfully when ISBN does not exist")
    void shouldCreateBookSuccessfully() {
        var newBook = new Book();
        newBook.setTitle("New Book");

        when(this.bookRepository.findByIsbn13(anyString())).thenReturn(Optional.empty());
        when(this.publisherService.findOrCreate(anyString())).thenReturn(this.testPublisher);
        when(this.bookMapper.toEntity(any(BookRequest.class))).thenReturn(newBook);
        when(this.bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            var book = invocation.getArgument(0, Book.class);
            book.setId(2L);
            return book;
        });
        when(this.authorService.create(anyString(), any(Book.class))).thenAnswer(invocation -> {
            var author = new Author(invocation.getArgument(0, String.class), invocation.getArgument(1, Book.class));
            author.setId(2L);
            return author;
        });
        when(this.bookMapper.toResponse(any(Book.class))).thenReturn(this.testResponse);

        var result = this.bookService.create(this.testRequest);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("New Book");
        assertThat(result.publisherName()).isEqualTo("Test Publisher");
        verify(this.bookRepository).save(any(Book.class));
        verify(this.authorService).create(anyString(), any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when book not found by id")
    void shouldThrowExceptionWhenBookNotFound() {
        when(this.bookRepository.findByIdWithPublisherAndAuthor(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found with id: 99");
    }

    @Test
    @DisplayName("Should throw exception when creating book with duplicate ISBN")
    void shouldThrowExceptionWhenDuplicateIsbn() {
        when(this.bookRepository.findByIsbn13(this.testRequest.isbn13())).thenReturn(Optional.of(this.testBook));

        assertThatThrownBy(() -> this.bookService.create(this.testRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }
}