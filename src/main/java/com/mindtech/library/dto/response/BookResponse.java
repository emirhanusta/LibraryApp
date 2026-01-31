package com.mindtech.library.dto.response;

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
}
