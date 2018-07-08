package org.vatalu.service;


import org.obsessive.web.lang.annotation.Inject;
import org.obsessive.web.lang.annotation.Service;
import org.obsessive.web.lang.annotation.Value;
import org.vatalu.Repository.UserRepository;
import org.vatalu.model.User;

@Service
public class UserService {
    @Inject
    private UserRepository userRepository;

    public User findUserById(String id) {
        //do something
        return userRepository.findUserById(Long.parseLong(id));
    }
}
