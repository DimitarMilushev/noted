package com.d_m.noted.config;

import com.d_m.noted.security.SecurityDetailsService;
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
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private static String[] LOGGED_OUT_ROUTES = {
            "/api/v1/auth/sign-in",
            "/api/v1/auth/sign-up",
            "/api/v1/auth/change-password"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        this.configureLogout(http);
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(LOGGED_OUT_ROUTES).anonymous()
                        .anyRequest().authenticated()
                )

                .sessionManagement((config) -> config
                        .addSessionAuthenticationStrategy(new ChangeSessionIdAuthenticationStrategy())
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(HeadersConfigurer::disable)
                .build();
    }

    private void configureLogout(HttpSecurity http) throws Exception {
        final var clearSiteDataHandler = new HeaderWriterLogoutHandler(
                new ClearSiteDataHeaderWriter(
                        ClearSiteDataHeaderWriter.Directive.COOKIES
                )
        );
        final var logoutSuccessHandler = new HttpStatusReturningLogoutSuccessHandler();
        http.logout(logout -> logout
                .logoutUrl("/api/v1/auth/sign-out")
                .addLogoutHandler(clearSiteDataHandler)
                .logoutSuccessHandler(logoutSuccessHandler)
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll()
        );
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
