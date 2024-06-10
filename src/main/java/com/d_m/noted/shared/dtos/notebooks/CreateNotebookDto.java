package com.d_m.noted.shared.dtos.notebooks;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateNotebookDto (
        @NotNull
        Long userId,
        @NotEmpty
        String title
){}
