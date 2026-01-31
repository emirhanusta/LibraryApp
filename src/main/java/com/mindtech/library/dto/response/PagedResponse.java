package com.mindtech.library.dto.response;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        @NotNull List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    @NotNull
    public static <T> PagedResponse<T> from(@NotNull final Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}