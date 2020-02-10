package it.mulders.futbolin.webapp.content;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static lombok.AccessLevel.PACKAGE;

@AllArgsConstructor(access = PACKAGE)
@Controller
@NoArgsConstructor
@Path("static")
@RequestScoped
@Slf4j
public class StaticPageController {
    @Inject ViewLocator viewLocator;

    @GET
    @Path("/{path}")
    @Produces("text/html; charset=UTF-8")
    public Response show(@PathParam("path") final String path) {
        var viewName = String.format("static/%s.jsp", path);
        if (hasViewWithName(viewName)) {
            return Response.ok().entity(viewName).build();
        } else {
            log.info("Requested page {} not found", path);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private boolean hasViewWithName(final String name) {
        return viewLocator.hasViewWithName(name);
    }
}
