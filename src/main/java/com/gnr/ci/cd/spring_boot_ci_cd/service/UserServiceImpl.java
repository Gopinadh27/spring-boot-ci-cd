package com.gnr.ci.cd.spring_boot_ci_cd.service;

import com.gnr.ci.cd.spring_boot_ci_cd.entity.User;
import com.gnr.ci.cd.spring_boot_ci_cd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
