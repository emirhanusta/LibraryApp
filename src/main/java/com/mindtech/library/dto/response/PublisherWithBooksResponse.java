package com.mindtech.library.dto.response;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PublisherWithBooksResponse(
        @NotNull Long id,
        @NotNull String name,
        @NotNull List<BookResponse> books
) {
}
