package com.d_m.noted.shared.dtos.notebooks;

import java.time.ZonedDateTime;

public record NotePreviewDetailsDto(
        Long id,
        String title,
        String content,
        ZonedDateTime dateCreated,
        ZonedDateTime lastUpdated
) {}
