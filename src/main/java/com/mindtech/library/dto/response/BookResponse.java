package com.mindtech.library.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookResponse(
        @NotNull Long id,
        @NotNull String title,
        @NotNull BigDecimal price,
        @NotNull String isbn13,
        @NotNull String publisherName,
        @NotNull String authorNameSurname,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull LocalDate publicationDate
) {
}
