package it.mulders.futbolin.webapp.content;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViewLocatorTest implements WithAssertions {

    private ServletContext servletContext = mock(ServletContext.class);
    private Configuration krazoConfiguration = mock(Configuration.class);

    private ViewLocator viewLocator = new ViewLocator(servletContext, krazoConfiguration);

    @Test
    void hasViewWithName_existingView_shouldReturnTrue() throws MalformedURLException {
        // We recognise an existing view by the fact that, when Krazo has resolved it,
        // the Servlet context can map it to a resource URL.
        when(servletContext.getResource(any())).thenReturn(new URL("file:////"));
        assertThat(viewLocator.hasViewWithName("whatever")).isTrue();
    }

    @Test
    void hasViewWithName_nonExistingView_shouldReturnFalse() throws MalformedURLException {
        // We recognise an existing view by the fact that, when Krazo has resolved it,
        // the Servlet context can map it to a resource URL.
        when(servletContext.getResource(any())).thenReturn(null);
        assertThat(viewLocator.hasViewWithName("whatever")).isFalse();
    }
}