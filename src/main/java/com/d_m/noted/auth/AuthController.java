package com.d_m.noted.auth;

import com.d_m.noted.shared.dtos.auth.ChangePasswordDto;
import com.d_m.noted.shared.dtos.auth.SignInDto;
import com.d_m.noted.shared.dtos.auth.SignUpDto;
import com.d_m.noted.users.UsersService;
import com.d_m.noted.users.entities.UserData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class AuthController {
    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    @Autowired
    public AuthController(
            UsersService usersService,
            AuthenticationManager authenticationManager
    ) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto payload) {
        final UserData user = this.usersService.createUser(payload);
        // return session
        return ResponseEntity.ok(user.toString());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(
            @RequestBody SignInDto payload, HttpServletRequest request, HttpServletResponse response
    ) {
        final Authentication authRequest =
                UsernamePasswordAuthenticationToken
                        .unauthenticated(
                                payload.email(),
                                payload.password()
                        );
        final Authentication authentication =  this.authenticationManager.authenticate(authRequest);
        final SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto payload) {
        this.usersService.changePasswordByEmail(payload);

        return ResponseEntity.ok("success");
    }
}
