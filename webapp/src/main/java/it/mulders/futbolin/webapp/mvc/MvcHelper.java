package it.mulders.futbolin.webapp.mvc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.krazo.jaxrs.JaxRsContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mvc.MvcContext;
import javax.ws.rs.core.UriInfo;

/**
 * Inspired by <a href="https://github.com/eclipse-ee4j/mvc-api/issues/56">eclipse-ee4j/mvc-api #56</a>.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Named
@NoArgsConstructor
@RequestScoped
@Slf4j
public class MvcHelper {
    @Inject
    private MvcContext mvcContext;

    @Inject
    @JaxRsContext
    private UriInfo uriInfo;

    public String getApplicationPath() {
        log.debug("URI Info - absolute path: {}", uriInfo.getAbsolutePath());
        log.debug("URI Info - base URI: {}", uriInfo.getBaseUri());
        log.debug("URI Info - path: {}", uriInfo.getPath());

        var basePath = mvcContext.getBasePath();
        log.debug("MVC Context - basePath: {}", basePath);

        var requestedPath = uriInfo.getPath();

        return String.format("%s/%s", basePath, requestedPath);
    }
}
