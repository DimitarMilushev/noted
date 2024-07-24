package com.d_m.noted.shared.dtos.notes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateNoteContentDto(
        @NotEmpty String title,
        @NotNull String content
) {}
