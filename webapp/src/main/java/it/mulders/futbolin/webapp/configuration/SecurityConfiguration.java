package it.mulders.futbolin.webapp.configuration;

import it.mulders.futbolin.webapp.security.pac4j.DefaultClientCallbackUrlResolver;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.oidc.client.GoogleOidcClient;
import org.pac4j.oidc.config.OidcConfiguration;

@Slf4j
public class SecurityConfiguration implements ConfigFactory {

    @Builder
    static final class SecurityParameters {
        final String callbackUrl;
        final String clientId;
        final String secret;
    }

    private static final class MergedOidcConfiguration extends MergedConfiguration {
        MergedOidcConfiguration() {
            super("/oidc.properties");
        }

        String getOidcClientId() {
            return getValue("OIDC_CLIENT_ID", "oidc.client.id");
        }

        String getOidcClientSecret() {
            return getValue("OIDC_CLIENT_SECRET", "oidc.client.secret");
        }

        String getOidcCallbackUrl() {
            return getValue("OIDC_CALLBACK_URL", "oidc.callback.url");
        }
    }

    private static final SecurityParameters params = loadSecurityParameters();

    static SecurityParameters loadSecurityParameters() {
        var configuration = new MergedOidcConfiguration();

        return SecurityParameters.builder()
                .callbackUrl(configuration.getOidcCallbackUrl())
                .clientId(configuration.getOidcClientId())
                .secret(configuration.getOidcClientSecret())
                .build();
    }

    private OidcConfiguration oidcConfiguration() {
        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId(params.clientId);
        oidcConfiguration.setSecret(params.secret);
        oidcConfiguration.setUseNonce(true);
        return oidcConfiguration;
    }

    private GoogleOidcClient googleOidcClient() {
        final GoogleOidcClient oidcClient = new GoogleOidcClient(oidcConfiguration());
        oidcClient.setAuthorizationGenerator((ctx, profile) -> {
            profile.addRole("ROLE_ADMIN");
            return profile;
        });
        oidcClient.setCallbackUrlResolver(new DefaultClientCallbackUrlResolver());
        oidcClient.setCallbackUrl(params.callbackUrl);
        return oidcClient;
    }

    @Override
    public Config build(Object... parameters) {
        final Clients clients = new Clients(
                "",
                googleOidcClient()
        );
        return new Config(clients);
    }
}
