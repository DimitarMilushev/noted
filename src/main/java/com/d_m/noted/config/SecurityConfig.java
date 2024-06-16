package com.d_m.noted.config;

import com.d_m.noted.security.SecurityDetailsService;
import com.d_m.noted.users.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;


@Configuration
@EnableWebSecurity()
public class SecurityConfig {

    private static String[] LOGGED_OUT_ROUTES = {
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/change-password"
    };

    private static String[] ADMIN_ROUTES = {
            "/h2-console"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP routes
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(LOGGED_OUT_ROUTES).anonymous()
                .requestMatchers(ADMIN_ROUTES).hasRole(UserRole.ADMIN.getStringifiedValue())
                .anyRequest().authenticated()
        );

        // Session management
        http.sessionManagement((config) -> config
                .addSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy())
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

        // CORS/CSRF
        //TODO: Adjust when frontend is ready
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable);

        // Logout
        http.logout(
                logout -> logout
                    .logoutUrl("/api/v1/auth/sign-out")
                    .addLogoutHandler(new HeaderWriterLogoutHandler(
                            new ClearSiteDataHeaderWriter(
                                    ClearSiteDataHeaderWriter.Directive.COOKIES
                            )
                    ))
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            SecurityDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
