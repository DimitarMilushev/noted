package com.d_m.noted.users;

import com.d_m.noted.notebooks.entities.Notebook;
import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNoteDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataNotebookDto;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataDto;
import com.d_m.noted.users.entities.UserData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "usersMapper")
public class UsersMapper {
    public LoadDashboardDataDto mapUserToLoadDashboardDataDto(UserData user) {
       final List<LoadDashboardDataNotebookDto> notebooks = user
               .getNotebooks()
               .stream()
               .map(this::mapNotebookToLoadDashboardDataNotebookDto)
               .toList();

        return LoadDashboardDataDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .notebooks(notebooks)
                .build();
    }

    private LoadDashboardDataNotebookDto mapNotebookToLoadDashboardDataNotebookDto(Notebook notebook) {
        final List<LoadDashboardDataNoteDto> notes = notebook
                .getNotes()
                .stream()
                .map(this::mapNoteToLoadDashboardDataNoteDto)
                .toList();

        return LoadDashboardDataNotebookDto.builder()
                .id(notebook.getId())
                .title(notebook.getTitle())
                .notes(notes)
                .lastUpdated(notebook.getUpdatedAt())
                .build();
    }

    private LoadDashboardDataNoteDto mapNoteToLoadDashboardDataNoteDto(Note note) {
        return LoadDashboardDataNoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .lastUpdated(note.getUpdatedAt())
                .build();
    }
}
