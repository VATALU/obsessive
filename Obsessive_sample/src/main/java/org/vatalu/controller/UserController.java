package org.vatalu.controller;

import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Inject;
import org.vatalu.service.UserService;

@Controller
public class UserController {
    @Inject
    private UserService userService;
}
