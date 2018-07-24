package org.vatalu.controller;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import org.obsessive.web.lang.annotation.*;
import org.vatalu.model.User;
import org.vatalu.service.UserService;

@Controller
public class UserController {
    @Inject
    private UserService userService;

    @Value("Obsessive")
    private String value;

    @Route(value = "/users/:userId", method = HttpMethod.GET)
    public void helloWorld(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("userId");
        User user = userService.findUserById(userId);
        routingContext.response().setChunked(true).write("Hello World " + user.getUserName() + ", Welcome to " + value).end();
    }
}
