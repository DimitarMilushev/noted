package com.d_m.noted.shared.dtos.notebooks;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AddNoteRefToNotebookDto(
        @Positive
        Long notebookId,
        @Positive
        Long noteId,
        @NotEmpty
        String title
) {
}
