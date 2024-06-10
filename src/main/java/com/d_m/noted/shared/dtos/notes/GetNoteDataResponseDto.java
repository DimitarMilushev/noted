package com.d_m.noted.shared.dtos.notes;

import com.d_m.noted.notes.enums.NoteSharedAccessLevel;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record GetNoteDataResponseDto(
        Long id,
        String title,
        String content,
        ZonedDateTime dateCreated,
        ZonedDateTime lastUpdated
) { }
