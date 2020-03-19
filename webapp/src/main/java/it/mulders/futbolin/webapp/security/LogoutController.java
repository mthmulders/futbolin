package it.mulders.futbolin.webapp.security;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.mvc.Controller;
import javax.mvc.View;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Controller
@NoArgsConstructor
@Path("logout")
@RequestScoped
@Slf4j
public class LogoutController {
    @GET
    @View("redirect:/home")
    public void logout(@Context final HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
