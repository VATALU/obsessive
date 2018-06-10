package org.vatalu.service;


import org.obsessive.web.lang.annotation.Immit;
import org.obsessive.web.lang.annotation.Service;
import org.obsessive.web.lang.annotation.Value;
import org.vatalu.Repository.UserRepository;

@Service
public class UserService {
    @Immit
    private UserRepository userRepository;
    @Value("123")
    private String value;
}
