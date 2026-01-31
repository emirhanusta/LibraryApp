package com.mindtech.library.dto.response;

import org.jetbrains.annotations.NotNull;

public record PublisherResponse(
        @NotNull Long id,
        @NotNull String name
) {
}
