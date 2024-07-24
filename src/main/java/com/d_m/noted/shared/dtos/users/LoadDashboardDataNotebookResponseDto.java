package com.d_m.noted.shared.dtos.users;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record LoadDashboardDataNotebookResponseDto(
        Long id,
        String title,
        ZonedDateTime lastUpdated,
        List<LoadDashboardDataNoteResponseDto> notes
) {}
