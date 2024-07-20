package com.d_m.noted.shared.dtos.notebooks;

import jakarta.validation.constraints.NotEmpty;

public record UpdateNotebookTitleDto(
        @NotEmpty
        String title
) {
}
