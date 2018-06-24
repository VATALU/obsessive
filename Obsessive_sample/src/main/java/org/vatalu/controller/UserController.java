package org.vatalu.controller;

import io.vertx.core.http.HttpMethod;
import org.obsessive.web.lang.annotation.*;
import org.vatalu.model.User;
import org.vatalu.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Inject
    private UserService userService;

    @Route(value = "/users/:userId", method = HttpMethod.POST)
    public User login(@BodyParam User users) {

        return null;
    }
}
