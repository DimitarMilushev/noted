package com.d_m.noted.users;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNoteDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNotebookDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataDto;
import com.d_m.noted.users.entities.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    LoadDashboardDataDto userToLoadDashboardDataDto(UserData user);

    @Mapping(target = "lastUpdated", source = "updatedAt")
    LoadDashboardDataNotebookDto notebookToLoadDashboardDataNotebookDto(Notebook notebook);

    @Mapping(target = "lastUpdated", source = "updatedAt")
    LoadDashboardDataNoteDto noteToLoadDashboardDataNoteDto(Note note);
}
