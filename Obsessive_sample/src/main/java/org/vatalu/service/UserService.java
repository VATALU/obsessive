package org.vatalu.service;


import org.obsessive.web.lang.annotation.Immit;
import org.obsessive.web.lang.annotation.Service;
import org.vatalu.Repository.UserRepository;

@Service
public class UserService {
    @Immit
    private UserRepository userRepository;
}
