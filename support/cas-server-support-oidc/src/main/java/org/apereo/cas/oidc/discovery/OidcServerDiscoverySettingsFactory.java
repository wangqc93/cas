package org.apereo.cas.oidc.discovery;

import lombok.val;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.support.oauth.OAuth20GrantTypes;
import org.apereo.cas.support.oauth.OAuth20ResponseTypes;
import org.apereo.cas.util.CollectionUtils;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;

/**
 * This is {@link OidcServerDiscoverySettingsFactory}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@Slf4j
@RequiredArgsConstructor
public class OidcServerDiscoverySettingsFactory implements FactoryBean<OidcServerDiscoverySettings> {
    private final CasConfigurationProperties casProperties;

    @Override
    public OidcServerDiscoverySettings getObject() {
        val oidc = casProperties.getAuthn().getOidc();
        val discoveryProperties =
            new OidcServerDiscoverySettings(casProperties, oidc.getIssuer());

        discoveryProperties.setClaimsSupported(oidc.getClaims());
        discoveryProperties.setScopesSupported(oidc.getScopes());
        discoveryProperties.setResponseTypesSupported(
            CollectionUtils.wrapList(OAuth20ResponseTypes.CODE.getType(),
                OAuth20ResponseTypes.TOKEN.getType(),
                OAuth20ResponseTypes.IDTOKEN_TOKEN.getType()));

        discoveryProperties.setSubjectTypesSupported(oidc.getSubjectTypes());
        discoveryProperties.setClaimTypesSupported(CollectionUtils.wrap("normal"));

        val authnMethods = new ArrayList<String>();
        authnMethods.add("client_secret_basic");
        discoveryProperties.setIntrospectionSupportedAuthenticationMethods(authnMethods);

        discoveryProperties.setGrantTypesSupported(
            CollectionUtils.wrapList(OAuth20GrantTypes.AUTHORIZATION_CODE.getType(),
                OAuth20GrantTypes.PASSWORD.getType(),
                OAuth20GrantTypes.CLIENT_CREDENTIALS.getType(),
                OAuth20GrantTypes.REFRESH_TOKEN.getType()));

        discoveryProperties.setIdTokenSigningAlgValuesSupported(CollectionUtils.wrapList("none", "RS256"));
        return discoveryProperties;
    }

    @Override
    public Class<?> getObjectType() {
        return OidcServerDiscoverySettings.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
