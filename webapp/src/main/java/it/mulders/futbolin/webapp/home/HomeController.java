package it.mulders.futbolin.webapp.home;

import it.mulders.futbolin.webapp.security.FutbolinUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
@Controller
@NoArgsConstructor
@Path("home")
@RequestScoped
@Slf4j
public class HomeController {
    @Inject Models model;
    @Inject OverviewService overviewService;

    @GET
    @Produces("text/html; charset=UTF-8")
    @View("home.jsp")
    public void show(@Context SecurityContext context) {
        if (context.getUserPrincipal() != null) {
            var userId = ((FutbolinUser) context.getUserPrincipal()).getId();
            log.info("Fetching overview for user {}", userId);

            model.put("teams", overviewService.getMyTeams());
            model.put("tournaments", overviewService.getMyTournaments());
            model.put("matches", overviewService.getMyMatches());
        }
    }
}
