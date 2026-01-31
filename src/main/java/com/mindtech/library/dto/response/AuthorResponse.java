package com.mindtech.library.dto.response;

import org.jetbrains.annotations.NotNull;

public record AuthorResponse(
        @NotNull Long id,
        @NotNull String nameSurname,
        @NotNull String bookTitle
) {
}
