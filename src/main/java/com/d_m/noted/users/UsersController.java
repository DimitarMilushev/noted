package com.d_m.noted.users;

import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataDto;
import com.d_m.noted.users.entities.UserData;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsersController {
    private final UsersService service;
    private final UsersMapper mapper;

    @GetMapping("/dashboard-data")
    public ResponseEntity<LoadDashboardDataDto> getUserWithNotebooks(
            @AuthenticationPrincipal UserSessionDetails user
    ) {
        final UserData userData = this.service.findById(user.getId());
        final LoadDashboardDataDto response = mapper.userToLoadDashboardDataDto(userData);
        return ResponseEntity.ok(response);
    }
}
