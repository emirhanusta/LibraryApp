package com.mindtech.library.dto.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public record GoogleBookResponse(
        @NotNull String title,
        @Nullable BigDecimal price,
        @Nullable String isbn13,
        @Nullable String publisherName,
        @Nullable String authorNameSurname
) {
}
