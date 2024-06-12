package com.d_m.noted.users;

import com.d_m.noted.users.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserData, Long> {
    boolean existsByEmail(String email);

    Optional<UserData> findByEmail(String email);
}
