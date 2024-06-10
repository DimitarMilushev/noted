package com.d_m.noted.users.dtos;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record LoadDashboardDataNotebookDto (
        Long id,
        String title,
        ZonedDateTime lastUpdated,
        List<LoadDashboardDataNoteDto> notes
) {}
