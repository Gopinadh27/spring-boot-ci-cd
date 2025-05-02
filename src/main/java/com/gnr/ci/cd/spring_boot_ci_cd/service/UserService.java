package com.gnr.ci.cd.spring_boot_ci_cd.service;

import com.gnr.ci.cd.spring_boot_ci_cd.entity.User;

public interface UserService {

     User registerUser(User user);

     User findByEmail(String email);
}
