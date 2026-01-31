package com.mindtech.library.client;

import com.mindtech.library.dto.response.GoogleBooksApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleBooksClient", url = "https://www.googleapis.com/books/v1")
public interface GoogleBooksClient {

    @GetMapping("/volumes")
    GoogleBooksApiResponse searchBooks(@RequestParam("q") String query);
}
