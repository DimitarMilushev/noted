package com.d_m.noted.shared.dtos.users;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadDashboardDataResponseDto(
        Long id,
        List<LoadDashboardDataNotebookResponseDto> notebooks
){ }
