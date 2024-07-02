package com.d_m.noted.auth;

import com.d_m.noted.shared.dtos.auth.SignInDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final SecurityContextHolderStrategy securityCtxHolder;
    private final SecurityContextRepository securityCtxRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager) {
        this.authManager = authenticationManager;
        this.securityCtxRepository = new HttpSessionSecurityContextRepository();
        this.securityCtxHolder = SecurityContextHolder.getContextHolderStrategy();
    }

    public Authentication authenticateUser(SignInDto payload, HttpServletRequest request, HttpServletResponse response) {
        final Authentication authRequest =
                UsernamePasswordAuthenticationToken
                        .unauthenticated(
                                payload.email(),
                                payload.password()
                        );
        final Authentication authentication =  this.authManager.authenticate(authRequest);
        final SecurityContext context = this.securityCtxHolder.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityCtxHolder.setContext(context);
        this.securityCtxRepository.saveContext(context, request, response);
        return authentication;
    }
}
