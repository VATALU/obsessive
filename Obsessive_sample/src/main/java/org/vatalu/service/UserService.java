package org.vatalu.service;


import org.obsessive.web.lang.annotation.Inject;
import org.obsessive.web.lang.annotation.Service;
import org.obsessive.web.lang.annotation.Value;
import org.vatalu.Repository.UserRepository;

@Service
public class UserService {
    @Inject
    private UserRepository userRepository;
    @Value("Obsessive")
    private String value;
}
