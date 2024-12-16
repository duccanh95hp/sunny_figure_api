package com.example.be.repository;

import com.example.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE DAY(u.birthday) = DAY(CURRENT_DATE) AND MONTH(u.birthday) = MONTH(CURRENT_DATE)")
    List<User> findUsersWithSameBirthday();

}
