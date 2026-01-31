package com.mindtech.library.mapper;

import com.mindtech.library.dto.response.PublisherResponse;
import com.mindtech.library.dto.response.PublisherWithBooksResponse;
import com.mindtech.library.entity.Publisher;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface PublisherMapper {

    @NotNull
    PublisherResponse toResponse(@NotNull Publisher publisher);

    @Mapping(target = "books", source = "books")
    @NotNull
    PublisherWithBooksResponse toResponseWithBooks(@NotNull Publisher publisher);
}