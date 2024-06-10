package com.d_m.noted.auth;

import com.d_m.noted.shared.dtos.auth.SignInDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.UsersService;
import com.d_m.noted.users.entities.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersService usersService;

    @Autowired
    public AuthController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto payload) {
        this.usersService.createUser(payload);
        // return session
        return ResponseEntity.ok("");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(
            // get session
            @RequestBody SignInDto payload
    ) {
        final UserData user = this.usersService.findByEmailAndPassword(payload);
        return ResponseEntity.ok(user.getId().toString());
    }
}
