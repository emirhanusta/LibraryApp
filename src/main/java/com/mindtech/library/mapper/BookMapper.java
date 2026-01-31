package com.mindtech.library.mapper;

import com.mindtech.library.dto.request.BookRequest;
import com.mindtech.library.dto.response.BookResponse;
import com.mindtech.library.entity.Author;
import com.mindtech.library.entity.Book;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "publisherName", source = "publisher.name")
    @Mapping(target = "authorNameSurname", source = "author", qualifiedByName = "authorToNameSurname")
    @NotNull
    BookResponse toResponse(@NotNull Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "author", ignore = true)
    @NotNull
    Book toEntity(@NotNull BookRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "author", ignore = true)
    void updateEntity(@NotNull BookRequest request, @MappingTarget @NotNull Book book);

    @Named("authorToNameSurname")
    @Nullable
    default String authorToNameSurname(@Nullable final Author author) {
        if (author == null) {
            return null;
        }
        return author.getNameSurname();
    }
}