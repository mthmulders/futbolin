package it.mulders.futbolin.webapp.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString(exclude = { "displayName", "email" })
public class DefaultFutbolinUser implements FutbolinUser {
    private final String id;
    private final String displayName;
    private final String email;
}
