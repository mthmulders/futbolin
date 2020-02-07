package it.mulders.futbolin.webapp.configuration;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.pac4j.oidc.client.GoogleOidcClient;

class SecurityConfigurationTest implements WithAssertions {
    private SecurityConfiguration configuration = new SecurityConfiguration();

    @Test
    void loadSecurityParameters_shouldLoadFromOidcPropertiesFile() {
        var parameters = configuration.loadSecurityParameters();

        assertThat(parameters.callbackUrl).isEqualTo("http://localhost:9080/callback");
        assertThat(parameters.clientId).isEqualTo("54879548932");
        assertThat(parameters.secret).isEqualTo("dhsudhbhfjksiofkjhsdazjky438iory43huicwga56jkxmfllsaATYfgszhduidks");
    }

    @Test
    void build_shouldDeliveryConfig() {
        var config = configuration.build();

        assertThat(config).isNotNull();
    }

    @Test
    void build_shouldDefineOneClient() {
        var config = configuration.build();

        assertThat(config).isNotNull();
        var clients = config.getClients().getClients();
        assertThat(clients).hasSize(1);
        assertThat(clients).hasOnlyElementsOfType(GoogleOidcClient.class);

        var client = (GoogleOidcClient) clients.get(0);
        assertThat(client.getConfiguration().getClientId()).isEqualTo("54879548932");
    }
}