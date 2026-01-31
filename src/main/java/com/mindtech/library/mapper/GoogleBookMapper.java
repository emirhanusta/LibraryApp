package com.mindtech.library.mapper;

import com.mindtech.library.dto.response.GoogleBookResponse;
import com.mindtech.library.dto.response.GoogleBooksApiResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface GoogleBookMapper {

    String ISBN_13_TYPE = "ISBN_13";

    @Mapping(target = "title", source = "volumeInfo.title", defaultValue = "Unknown")
    @Mapping(target = "price", source = "saleInfo", qualifiedByName = "extractPrice")
    @Mapping(target = "isbn13", source = "volumeInfo", qualifiedByName = "extractIsbn13")
    @Mapping(target = "publisherName", source = "volumeInfo.publisher")
    @Mapping(target = "authorNameSurname", source = "volumeInfo", qualifiedByName = "extractAuthor")
    GoogleBookResponse toResponse(@NotNull GoogleBooksApiResponse.Item item);

    @Named("extractPrice")
    @Nullable
    default BigDecimal extractPrice(@Nullable final GoogleBooksApiResponse.SaleInfo saleInfo) {
        if (saleInfo == null || saleInfo.listPrice() == null) {
            return null;
        }
        var amount = saleInfo.listPrice().amount();
        return amount != null ? BigDecimal.valueOf(amount) : null;
    }

    @Named("extractIsbn13")
    @Nullable
    default String extractIsbn13(@Nullable final GoogleBooksApiResponse.VolumeInfo volumeInfo) {
        if (volumeInfo == null || volumeInfo.industryIdentifiers() == null) {
            return null;
        }
        return volumeInfo.industryIdentifiers().stream()
                .filter(id -> ISBN_13_TYPE.equals(id.type()))
                .map(GoogleBooksApiResponse.IndustryIdentifier::identifier)
                .findFirst()
                .orElse(null);
    }

    @Named("extractAuthor")
    @Nullable
    default String extractAuthor(@Nullable final GoogleBooksApiResponse.VolumeInfo volumeInfo) {
        if (volumeInfo == null || volumeInfo.authors() == null || volumeInfo.authors().isEmpty()) {
            return null;
        }
        return volumeInfo.authors().getFirst();
    }
}