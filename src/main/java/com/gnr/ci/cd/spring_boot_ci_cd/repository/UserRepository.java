package com.gnr.ci.cd.spring_boot_ci_cd.repository;

import com.gnr.ci.cd.spring_boot_ci_cd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
