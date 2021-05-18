package it.mulders.futbolin.webapp.mvc;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import javax.mvc.MvcContext;
import javax.ws.rs.core.UriInfo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MvcHelperTest implements WithAssertions {
    private final MvcContext mvcContext = mock(MvcContext.class);
    private final UriInfo uriInfo = mock(UriInfo.class);

    private final MvcHelper helper = new MvcHelper(mvcContext, uriInfo);

    @Test
    void applicationPath_should_combine_baseContextPath_and_uriPath() {
        // Arrange
        when(mvcContext.getBasePath()).thenReturn("/app");
        when(uriInfo.getPath()).thenReturn("some/path/elsewhere");

        // Act
        var result = helper.getApplicationPath();

        // Assert
        assertThat(result).isEqualTo("/app/some/path/elsewhere");
    }
}