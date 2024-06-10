package com.d_m.noted.users;

import com.d_m.noted.users.dtos.LoadDashboardDataDto;
import com.d_m.noted.users.entities.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService service;
    private final UsersMapper mapper;

    @Autowired
    public UsersController(UsersService service, UsersMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    //TODO: change when auth is implemented
    @GetMapping("/user-with-notebooks/{id}")
    public ResponseEntity<LoadDashboardDataDto> getUserWithNotebooks(
            @PathVariable Long id
    ) {
        final UserData user = this.service.findById(id);
        final LoadDashboardDataDto response = mapper.mapUserToLoadDashboardDataDto(user);
        return ResponseEntity.ok(response);
    }
}
