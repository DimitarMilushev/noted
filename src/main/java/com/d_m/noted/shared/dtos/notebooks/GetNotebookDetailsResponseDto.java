package com.d_m.noted.shared.dtos.notebooks;

import java.time.ZonedDateTime;
import java.util.List;

public record GetNotebookDetailsResponseDto(
        Long id,
        String title,
        ZonedDateTime dateCreated,
        ZonedDateTime lastUpdated,
        List<NotePreviewDetailsDto> notes
) { }

