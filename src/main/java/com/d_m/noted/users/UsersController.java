package com.d_m.noted.users;

import com.d_m.noted.security.models.SecurityAuthDetails;
import com.d_m.noted.shared.dtos.users.LoadDashboardDataDto;
import com.d_m.noted.users.entities.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService service;
    private final UsersMapper mapper;

    @Autowired
    public UsersController(UsersService service, UsersMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/dashboard-data")
    public ResponseEntity<LoadDashboardDataDto> getUserWithNotebooks(
            @AuthenticationPrincipal SecurityAuthDetails user
    ) {
        final UserData userData = this.service.findById(user.getId());
        final LoadDashboardDataDto response = mapper.userToLoadDashboardDataDto(userData);
        return ResponseEntity.ok(response);
    }
}
