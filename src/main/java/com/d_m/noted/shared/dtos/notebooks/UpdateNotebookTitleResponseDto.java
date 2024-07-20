package com.d_m.noted.shared.dtos.notebooks;

import java.time.ZonedDateTime;

public record UpdateNotebookTitleResponseDto(
        String title,
        ZonedDateTime lastUpdated
) {
}
