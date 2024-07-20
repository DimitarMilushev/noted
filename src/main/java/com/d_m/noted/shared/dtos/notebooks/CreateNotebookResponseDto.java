package com.d_m.noted.shared.dtos.notebooks;

import java.time.ZonedDateTime;

public record CreateNotebookResponseDto (
        Long id,
        String title,
        ZonedDateTime dateCreated,
        ZonedDateTime lastUpdated
){ }
