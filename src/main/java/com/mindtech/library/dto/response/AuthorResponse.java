package com.mindtech.library.dto.response;

import com.mindtech.library.entity.Author;
import org.jetbrains.annotations.NotNull;

public record AuthorResponse(
        @NotNull Long id,
        @NotNull String nameSurname,
        @NotNull String bookTitle
) {

    @NotNull
    public static AuthorResponse fromEntity(@NotNull final Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getNameSurname(),
                author.getBook().getTitle()
        );
    }
}
