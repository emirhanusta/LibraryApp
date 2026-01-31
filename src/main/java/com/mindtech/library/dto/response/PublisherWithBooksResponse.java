package com.mindtech.library.dto.response;

import com.mindtech.library.entity.Publisher;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PublisherWithBooksResponse(
        @NotNull Long id,
        @NotNull String name,
        @NotNull List<BookResponse> books
) {

    @NotNull
    public static PublisherWithBooksResponse fromEntity(@NotNull final Publisher publisher) {
        var bookResponses = publisher.getBooks().stream()
                .map(BookResponse::fromEntity)
                .toList();
        return new PublisherWithBooksResponse(publisher.getId(), publisher.getName(), bookResponses);
    }
}
