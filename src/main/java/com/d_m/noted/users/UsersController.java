package com.d_m.noted.users;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataResponseDto;
import com.d_m.noted.users.entities.UserData;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Validated
public class UsersController {
    private final UsersService service;
    private final UsersMapper mapper;

    @GetMapping("/dashboard-data")
    public ResponseEntity<LoadDashboardDataResponseDto> getUserWithNotebooks(
            @AuthenticationPrincipal UserPrincipal user
    ) {
        final UserData userData = this.service.getById(user.getId(), user);
        final LoadDashboardDataResponseDto response = mapper.userToLoadDashboardDataResponseDto(userData);
        return ResponseEntity.ok(response);
    }
}
