package org.vatalu.controller;

import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Immit;
import org.vatalu.service.UserService;

@Controller
public class UserController {
    @Immit
    private UserService userService;
}
