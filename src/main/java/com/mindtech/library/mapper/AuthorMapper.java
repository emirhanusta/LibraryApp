package com.mindtech.library.mapper;

import com.mindtech.library.dto.response.AuthorResponse;
import com.mindtech.library.entity.Author;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "bookTitle", source = "book.title")
    AuthorResponse toResponse(@NotNull Author author);
}