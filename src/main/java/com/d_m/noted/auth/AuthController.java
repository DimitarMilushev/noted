package com.d_m.noted.auth;

import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.shared.dtos.auth.ChangePasswordDto;
import com.d_m.noted.shared.dtos.auth.SignInDto;
import com.d_m.noted.shared.dtos.auth.SignInResponseDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.UsersService;
import com.d_m.noted.users.entities.UserData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final UsersService usersService;
    private final AuthMapper mapper;
    private final AuthService service;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpDto payload) {
        this.usersService.createUser(payload);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(
            @RequestBody SignInDto payload, HttpServletRequest request, HttpServletResponse response
    ) {
        final Authentication auth = service.authenticateUser(payload, request, response);
        final UserSessionDetails sessionDetails = (UserSessionDetails) auth.getPrincipal();

        final SignInResponseDto responseDto = mapper.userSessionDetailsToSignInResponseDto(sessionDetails);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto payload) {
        this.usersService.changePasswordByEmail(payload);

        return ResponseEntity.ok("success");
    }

}
