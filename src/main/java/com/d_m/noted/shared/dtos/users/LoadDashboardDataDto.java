package com.d_m.noted.shared.dtos.users;

import lombok.Builder;

import java.util.List;

@Builder
public record LoadDashboardDataDto (
        Long id,
        List<LoadDashboardDataNotebookDto> notebooks
){ }
