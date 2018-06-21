package org.vatalu.controller;

import io.vertx.core.http.HttpMethod;
import org.obsessive.web.lang.annotation.*;
import org.vatalu.model.User;
import org.vatalu.service.UserService;

@Controller
public class UserController {
    @Inject
    private UserService userService;

    @Route(value="/users/:userId",method = HttpMethod.POST)
    public User login(@PathParam("userId")String userId, @RequestParam("password")String password){
        System.out.println(userId+" "+password);
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        return user;
    }
}
