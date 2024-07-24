package com.d_m.noted.shared.dtos.notes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateNoteSharedStatusDto(
        @Positive Long noteId,
        @NotNull boolean isShared
) {
}
