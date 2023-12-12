/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Playtika
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.playtika.test.keycloak.vanilla;

import static com.playtika.test.keycloak.KeycloakProperties.DEFAULT_REALM;
import static com.playtika.test.keycloak.util.KeycloakClient.newKeycloakClient;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import com.playtika.test.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VanillaTestApplication.class)
@ActiveProfiles("enabled")
public class EmbeddedKeycloakBootstrapConfigurationTest {

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private KeycloakContainer keycloakContainer;

    @Test
    public void propertiesAreAvailable() {
        assertThat(environment.getProperty("embedded.keycloak.auth-server-url"))
            .isEqualTo(format("http://%s:%d/auth", keycloakContainer.getContainerIpAddress(),
                keycloakContainer.getHttpPort()));

        assertThat(environment.getProperty("embedded.keycloak.host"))
            .isEqualTo(keycloakContainer.getIp());

        assertThat(environment.getProperty("embedded.keycloak.http-port", Integer.class))
            .isEqualTo(keycloakContainer.getHttpPort());
    }

    @Test
    public void shouldGetMasterRealmInfoFromKeycloak() {
        String realmInfo = newKeycloakClient(environment).getRealmInfo(DEFAULT_REALM).getRealm();
        assertThat(realmInfo).isEqualTo(DEFAULT_REALM);
    }
}
