package it.mulders.futbolin.webapp.home;

import it.mulders.futbolin.webapp.security.MockSecurityContext;
import org.assertj.core.api.WithAssertions;
import org.eclipse.krazo.core.ModelsImpl;
import org.junit.jupiter.api.Test;

import javax.mvc.Models;
import javax.ws.rs.core.SecurityContext;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeControllerTest implements WithAssertions {
    private final OverviewService service = mock(OverviewService.class);
    private final Models model = new ModelsImpl();

    private final HomeController controller = new HomeController(model, service);

    private final SecurityContext securityContext = MockSecurityContext.loggedIn();

    @Test
    @SuppressWarnings("unchecked")
    void whenLoggedIn_show_modelShouldHaveTeams() {
        // Arrange
        final Collection<String> teams = List.of("My Team");
        when(service.getMyTeams()).thenReturn(teams);

        // Act
        controller.show(securityContext);

        // Assert
        assertThat(controller.model.get("teams")).isInstanceOf(Collection.class);
        assertThat((Collection<String>) controller.model.get("teams")).containsAll(teams);
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenLoggedIn_show_modelShouldHaveTournaments() {
        // Arrange
        final Collection<String> tournaments = List.of("My Tournament");
        when(service.getMyTournaments()).thenReturn(tournaments);

        // Act
        controller.show(securityContext);

        // Assert
        assertThat(controller.model.get("teams")).isInstanceOf(Collection.class);
        assertThat((Collection<String>) controller.model.get("tournaments")).containsAll(tournaments);
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenLoggedIn_show_modelShouldHaveMatches() {
        // Arrange
        final Collection<String> matches = List.of("My Match");
        when(service.getMyMatches()).thenReturn(matches);

        // Act
        controller.show(securityContext);

        // Assert
        assertThat(controller.model.get("matches")).isInstanceOf(Collection.class);
        assertThat((Collection<String>) controller.model.get("matches")).containsAll(matches);
    }

    @Test
    void whenNotLoggedIn_show_modelShouldBeEmpty() {
        // Act
        controller.show(MockSecurityContext.notLoggedIn());

        // Assert
        assertThat(controller.model).isEmpty();
    }
}