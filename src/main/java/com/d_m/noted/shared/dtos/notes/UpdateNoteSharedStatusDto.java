package com.d_m.noted.shared.dtos.notes;

import jakarta.validation.constraints.NotNull;

public record UpdateNoteSharedStatusDto(
        @NotNull
        Long noteId,
        @NotNull
        boolean isShared
) {
}
