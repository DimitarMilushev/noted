package com.d_m.noted.users.dtos;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record LoadDashboardDataNoteDto (
    Long id,
    String title,
    ZonedDateTime lastUpdated
){}
