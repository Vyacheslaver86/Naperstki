package com.naperstky.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount,Long> {

    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByNickname(String nickname);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}


