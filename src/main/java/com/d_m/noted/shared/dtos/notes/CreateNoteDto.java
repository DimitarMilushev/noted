package com.d_m.noted.shared.dtos.notes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateNoteDto(
        @NotEmpty
        String title,
        @Positive
        Long notebookId,
        @NotNull
        String content
) { }
