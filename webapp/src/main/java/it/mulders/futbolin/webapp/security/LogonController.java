package it.mulders.futbolin.webapp.security;

import it.mulders.futbolin.webapp.messaging.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
@Controller
@NoArgsConstructor
@Path("logon")
@RequestScoped
@Slf4j
public class LogonController {
    @Inject UserService userService;

    @GET
    @View("redirect:/home")
    public void login(@Context final SecurityContext context) throws MessagingException {
        // Nothing to do. The rules in web.xml have triggered the login flow.
        userService.notifyUserLoggedIn((FutbolinUser) context.getUserPrincipal());
    }
}
