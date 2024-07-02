package com.d_m.noted.auth;

import com.d_m.noted.auth.models.UserSessionDetails;
import com.d_m.noted.users.UsersRepository;
import com.d_m.noted.users.entities.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public UserSessionService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserData userData = this.usersRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to find user " + username));

        return UserSessionDetails.fromUserData(userData);
    }
}
