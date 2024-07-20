package com.d_m.noted.notebooks;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notebooks.GetNotebookByUserIdResponseDto;
import com.d_m.noted.shared.dtos.notebooks.GetNotebookDetailsResponseDto;
import com.d_m.noted.shared.dtos.notebooks.NotePreviewDetailsDto;
import com.d_m.noted.shared.dtos.notebooks.UpdateNotebookTitleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotebooksMapper {
    NotebooksMapper INSTANCE = Mappers.getMapper(NotebooksMapper.class);

    @Mapping(target = "dateCreated", source = "createdAt")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    GetNotebookDetailsResponseDto notebookToGetNotebookDetailsResponseDto(Notebook notebook);

    @Mapping(target = "dateCreated", source = "createdAt")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    NotePreviewDetailsDto noteToNotePreviewDetailsDto(Note notebook);

    @Mapping(target = "dateCreated", source = "createdAt")
    @Mapping(target = "lastUpdated", source = "updatedAt")
    GetNotebookByUserIdResponseDto notebookToGetNotebookByUserIdResponseDto(Notebook notebook);

    @Mapping(target = "lastUpdated", source = "updatedAt")
    UpdateNotebookTitleResponseDto notebookToUpdateNotebookTitleResponseDto(Notebook notebook);
}
