package org.vatalu.controller;

import io.vertx.core.http.HttpServerResponse;
import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Inject;
import org.obsessive.web.lang.annotation.Route;
import org.vatalu.service.UserService;
import io.vertx.ext.web.RoutingContext;

@Controller
public class UserController {
    @Inject
    private UserService userService;

    @Route("/some/path")
    public void login(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.write("{\"user\":\"vatalu\"}");
    }
}
