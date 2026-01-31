package com.mindtech.library.service;

import com.mindtech.library.dto.response.GoogleBooksApiResponse;
import com.mindtech.library.client.GoogleBooksClient;
import com.mindtech.library.dto.response.GoogleBookResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleBooksService {

    private static final String ISBN_13_TYPE = "ISBN_13";

    private final GoogleBooksClient googleBooksClient;

    public GoogleBooksService(@NotNull final GoogleBooksClient googleBooksClient) {
        this.googleBooksClient = googleBooksClient;
    }

    @NotNull
    public List<GoogleBookResponse> searchBooks(@NotNull final String query) {
        var response = this.googleBooksClient.searchBooks(query);

        if (response == null || response.items() == null) {
            return Collections.emptyList();
        }

        return response.items().stream()
                .map(this::mapToGoogleBookResponse)
                .toList();
    }

    @NotNull
    private GoogleBookResponse mapToGoogleBookResponse(@NotNull final GoogleBooksApiResponse.Item item) {
        var volumeInfo = item.volumeInfo();
        var title = volumeInfo != null ? volumeInfo.title() : "Unknown";
        var price = this.extractPrice(item.saleInfo());
        var isbn13 = this.extractIsbn13(volumeInfo);
        var publisher = volumeInfo != null ? volumeInfo.publisher() : null;
        var author = this.extractAuthor(volumeInfo);

        return new GoogleBookResponse(title != null ? title : "Unknown", price, isbn13, publisher, author);
    }

    @Nullable
    private BigDecimal extractPrice(@Nullable final GoogleBooksApiResponse.SaleInfo saleInfo) {
        if (saleInfo == null || saleInfo.listPrice() == null) {
            return null;
        }
        var amount = saleInfo.listPrice().amount();
        return amount != null ? BigDecimal.valueOf(amount) : null;
    }

    @Nullable
    private String extractIsbn13(@Nullable final GoogleBooksApiResponse.VolumeInfo volumeInfo) {
        if (volumeInfo == null || volumeInfo.industryIdentifiers() == null) {
            return null;
        }
        return volumeInfo.industryIdentifiers().stream()
                .filter(id -> ISBN_13_TYPE.equals(id.type()))
                .map(GoogleBooksApiResponse.IndustryIdentifier::identifier)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    private String extractAuthor(@Nullable final GoogleBooksApiResponse.VolumeInfo volumeInfo) {
        if (volumeInfo == null || volumeInfo.authors() == null || volumeInfo.authors().isEmpty()) {
            return null;
        }
        return volumeInfo.authors().getFirst();
    }
}
