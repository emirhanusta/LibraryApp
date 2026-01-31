package com.mindtech.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.exception.custom.ResourceNotFoundException;
import com.mindtech.library.service.BookService;
import com.mindtech.library.service.GoogleBooksService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private GoogleBooksService googleBooksService;

    @Test
    @DisplayName("Should return 404 when book not found")
    void shouldReturn404WhenBookNotFound() throws Exception {
        when(this.bookService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Book not found with id: 999"));

        this.mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {
        var invalidRequest = new BookRequest(
                "",
                null,
                "invalid",
                "",
                "",
                null
        );

        this.mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    @DisplayName("Should create book successfully with valid request")
    void shouldCreateBookSuccessfully() throws Exception {
        var request = new BookRequest(
                "Clean Code",
                BigDecimal.valueOf(45.00),
                "9780132350884",
                "Prentice Hall",
                "Robert C. Martin",
                null
        );

        var response = new BookResponse(
                1L,
                "Clean Code",
                BigDecimal.valueOf(45.00),
                "9780132350884",
                "Prentice Hall",
                "Robert C. Martin",
                null
        );

        when(this.bookService.create(any(BookRequest.class))).thenReturn(response);

        this.mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.authorNameSurname").value("Robert C. Martin"));
    }
}
