package com.d_m.noted.users;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNoteResponseDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNotebookResponseDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataResponseDto;
import com.d_m.noted.users.entities.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    LoadDashboardDataResponseDto userToLoadDashboardDataResponseDto(UserData user);

    @Mapping(target = "lastUpdated", source = "updatedAt")
    LoadDashboardDataNotebookResponseDto notebookToLoadDashboardDataNotebookResponseDto(Notebook notebook);

    @Mapping(target = "lastUpdated", source = "updatedAt")
    LoadDashboardDataNoteResponseDto noteToLoadDashboardDataNoteResponseDto(Note note);
}
