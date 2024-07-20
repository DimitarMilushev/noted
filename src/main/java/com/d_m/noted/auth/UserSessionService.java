package com.d_m.noted.auth;

import com.d_m.noted.auth.models.UserPrincipal;
import com.d_m.noted.users.UsersRepository;
import com.d_m.noted.users.entities.UserData;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSessionService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserData userData = this.usersRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to find user " + username));

        return UserPrincipal.fromUserData(userData);
    }
}
