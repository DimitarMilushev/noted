package com.d_m.noted.shared.dtos.notes;

import jakarta.validation.constraints.NotEmpty;

public record CreateNoteDto(
        @NotEmpty
        String title,
        @NotEmpty
        Long notebookId,
        String content
) { }
