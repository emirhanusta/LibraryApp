package com.mindtech.library.dto.response;

import com.mindtech.library.entity.Book;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookResponse(
        @NotNull Long id,
        @NotNull String title,
        @NotNull BigDecimal price,
        @NotNull String isbn13,
        @NotNull String publisherName,
        @Nullable String authorNameSurname,
        @Nullable LocalDate publicationDate
) {

    @NotNull
    public static BookResponse fromEntity(@NotNull final Book book) {
        var authorName = book.getAuthor() != null ? book.getAuthor().getNameSurname() : null;
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getPrice(),
                book.getIsbn13(),
                book.getPublisher().getName(),
                authorName,
                book.getPublicationDate()
        );
    }
}
