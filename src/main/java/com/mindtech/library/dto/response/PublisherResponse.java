package com.mindtech.library.dto.response;

import com.mindtech.library.entity.Publisher;
import org.jetbrains.annotations.NotNull;

public record PublisherResponse(
        @NotNull Long id,
        @NotNull String name
) {

    @NotNull
    public static PublisherResponse fromEntity(@NotNull final Publisher publisher) {
        return new PublisherResponse(publisher.getId(), publisher.getName());
    }
}
