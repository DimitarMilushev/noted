package com.d_m.noted.users.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadDashboardDataDto (
        Long id,
    String email,
    String username,
        List<LoadDashboardDataNotebookDto> notebooks
){ }
