package it.mulders.futbolin.webapp.content;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StaticPageControllerTest implements WithAssertions {
    private ViewLocator viewLocator = mock(ViewLocator.class);

    private StaticPageController controller = new StaticPageController(viewLocator);

    @Test
    void show_existingView_withExistingView_shouldRender() {
        when(viewLocator.hasViewWithName(any())).thenReturn(true);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void show_existingView_withNonExistingView_shouldFail() {
        when(viewLocator.hasViewWithName(any())).thenReturn(false);

        var response = controller.show("whatever");

        assertThat(response.getStatus()).isEqualTo(404);
    }
}