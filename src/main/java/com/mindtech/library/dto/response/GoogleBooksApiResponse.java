package com.mindtech.library.dto.response;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record GoogleBooksApiResponse(
        @Nullable Integer totalItems,
        @Nullable List<Item> items
) {

    public record Item(
            @Nullable String id,
            @Nullable VolumeInfo volumeInfo,
            @Nullable SaleInfo saleInfo
    ) {
    }

    public record VolumeInfo(
            @Nullable String title,
            @Nullable List<String> authors,
            @Nullable String publisher,
            @Nullable List<IndustryIdentifier> industryIdentifiers
    ) {
    }

    public record IndustryIdentifier(
            @Nullable String type,
            @Nullable String identifier
    ) {
    }

    public record SaleInfo(
            @Nullable ListPrice listPrice
    ) {
    }

    public record ListPrice(
            @Nullable Double amount,
            @Nullable String currencyCode
    ) {
    }
}
