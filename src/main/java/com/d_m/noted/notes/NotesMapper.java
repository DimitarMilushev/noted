package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotesMapper {
    NotesMapper INSTANCE = Mappers.getMapper(NotesMapper.class);

    @Mapping(target = "dateCreated", source = "createdAt")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    GetNoteDataResponseDto noteToGetNoteDataResponseDto(Note note);
}
