package it.mulders.futbolin.webapp.home;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequestScoped
@Slf4j
public class OverviewService {
    public Collection<String> getMyTeams() {
        return List.of("My Team");
    }

    public Collection<String> getMyTournaments() {
        return List.of("Example Tournament");
    }

    public Collection<String> getMyMatches() {
        return Collections.emptyList();
    }
}
