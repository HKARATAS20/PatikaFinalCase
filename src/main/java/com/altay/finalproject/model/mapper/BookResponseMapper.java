package com.altay.finalproject.model.mapper;

import com.altay.finalproject.model.dto.response.BookResponse;
import com.altay.finalproject.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    BookResponseMapper INSTANCE = Mappers.getMapper(BookResponseMapper.class);

    // @Mapping(target = "myTitle", source = "title")
    BookResponse toDTO(Book book);

    List<BookResponse> toDTOList(List<Book> books);
}

