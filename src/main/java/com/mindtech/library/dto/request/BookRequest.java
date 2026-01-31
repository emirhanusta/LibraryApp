package com.mindtech.library.dto.request;

import jakarta.validation.constraints.*;

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
        @Size(min = 13, max = 13, message = "ISBN13 must be exactly 13 characters")
        @Pattern(regexp = "\\d{13}", message = "ISBN13 must contain only digits")
        String isbn13,

        @NotBlank(message = "Publisher name is required")
        String publisherName,

        @NotBlank(message = "Author name is required")
        String authorNameSurname,

        LocalDate publicationDate
) {
}