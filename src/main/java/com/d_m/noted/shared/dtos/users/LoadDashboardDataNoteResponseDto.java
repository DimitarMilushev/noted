package com.d_m.noted.shared.dtos.users;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record LoadDashboardDataNoteResponseDto(
    Long id,
    String title,
    ZonedDateTime lastUpdated
){}
