package com.d_m.noted.shared.dtos.notebooks;

import lombok.Builder;

@Builder
public record AddNoteRefToNotebookDto(
    Long notebookId,
    Long noteId,
    String title
)
{}
