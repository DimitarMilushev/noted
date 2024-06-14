package com.d_m.noted.config;

import com.d_m.noted.security.SecurityDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static String[] PUBLIC_ROUTES = {
            "/auth/sign-in",
            "/auth/sign-up",
            "/auth/change-password",
            "/h2-console" //TODO: REMOVE
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        this.configureLogout(http);
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement((config) -> config
                        .sessionFixation().changeSessionId()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(HeadersConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
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
                .logoutUrl("/auth/sign-out")
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
