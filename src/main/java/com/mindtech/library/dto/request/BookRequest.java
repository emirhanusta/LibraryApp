package com.mindtech.library.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotBlank(message = "ISBN13 is required")
        @Pattern(regexp = "^[0-9]{13}$", message = "ISBN13 must be exactly 13 digits")
        String isbn13,

        @NotBlank(message = "Publisher name is required")
        String publisherName,

        @NotBlank(message = "Author name is required")
        String authorNameSurname,

        LocalDate publicationDate
) {
}
