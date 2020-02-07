package it.mulders.futbolin.webapp.content;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.krazo.engine.JspViewEngine;
import org.eclipse.krazo.engine.ViewEngineContextImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;

import java.net.MalformedURLException;

import static lombok.AccessLevel.PACKAGE;

/**
 * Krazo-specific
 */
@ApplicationScoped
@AllArgsConstructor(access = PACKAGE)
@NoArgsConstructor
@Slf4j
public class ViewLocator extends JspViewEngine {
    @Inject ServletContext servletContext;

    /** The Krazo configuration */
    @Context Configuration config;

    public boolean hasViewWithName(final String name) {
        var viewEngineContext = new ViewEngineContextImpl(name, null, null, null, null, null, null, null, null, config, null);

        var resolvedView = super.resolveView(viewEngineContext);
        log.debug("Resolved view {} to {}", name, resolvedView);

        try {
            var resource = servletContext.getResource(resolvedView);
            return resource != null;
        } catch (MalformedURLException murle) {
            log.warn("Resolved view {} is not in the expected form", resolvedView);
            return false;
        }
    }
}
