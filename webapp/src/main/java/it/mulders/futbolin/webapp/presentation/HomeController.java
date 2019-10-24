package it.mulders.futbolin.webapp.presentation;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Controller
@Path("home")
@RequestScoped
@Slf4j
public class HomeController {
    @Inject
    Models models;

    @GET
    @Produces("text/html; charset=UTF-8")
    @View("home.html")
    public void show() {
        log.info("Welcome home!");
    }
}
