package com.d_m.noted.notes;

import com.d_m.noted.notes.entities.Note;
import com.d_m.noted.shared.dtos.notes.GetNoteDataResponseDto;
import org.springframework.stereotype.Component;

@Component(value = "notesMapper")
public class NotesMapper {
    public GetNoteDataResponseDto mapNoteToGetNoteDataResponseDto(Note note) {
        return GetNoteDataResponseDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .dateCreated(note.getCreatedAt())
                .lastUpdated(note.getUpdatedAt())
                .build();
    }
}
