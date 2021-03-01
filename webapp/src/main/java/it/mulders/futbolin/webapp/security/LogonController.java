package it.mulders.futbolin.webapp.security;

import jakarta.mvc.Controller;
import jakarta.mvc.View;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Controller
@NoArgsConstructor
@Path("logon")
@RequestScoped
@Slf4j
public class LogonController {

    @GET
    @View("redirect:/home")
    public void login() {
        // Nothing to do. The rules in web.xml will trigger the login flow.
    }
}
