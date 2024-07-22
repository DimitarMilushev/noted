package com.d_m.noted.config;

import com.d_m.noted.auth.UserSessionService;
import com.d_m.noted.exception_handling.AuthenticationExceptionHandler;
import com.d_m.noted.users.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:8200/"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        // HTTP routes
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(LOGGED_OUT_ROUTES).anonymous()
                .requestMatchers(ADMIN_ROUTES).hasRole(UserRole.ADMIN.getStringifiedValue())
                .anyRequest().authenticated()
        );
        http.exceptionHandling((handling) -> handling.authenticationEntryPoint((new AuthenticationExceptionHandler())));

        // Session management
        http.sessionManagement((config) -> config
                .addSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy())
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );


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
            UserSessionService userDetailsService,
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
