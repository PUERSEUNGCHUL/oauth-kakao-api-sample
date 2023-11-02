package kr.co.puerpuella.oathserver.api.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialUtil {

    private final Environment env;

    public String getLoginPageURL(String provider) {
        String authorizationUri = env.getProperty("spring.security.oauth2.client.provider." + provider + ".authorization-uri");
        StringBuilder sb = new StringBuilder();
        sb.append(authorizationUri);
        sb.append("?client_id=");
        sb.append(getSecretKey(provider));
        sb.append("&redirect_uri=");
        sb.append(getRedirectUri(provider));
        sb.append("&response_type=code");
        return sb.toString();
    }

    public String getTokenProviderURL(String provider) {
        String tokenURL = env.getProperty("spring.security.oauth2.client.provider." + provider + ".token-uri");

        return tokenURL;
    }

    public String getClientId(String provider) {
        String clientId = env.getProperty("spring.security.oauth2.client.registration." + provider + ".client-id");
        return clientId;
    }

    public String getSecretKey(String provider) {
        String clientId = env.getProperty("spring.security.oauth2.client.registration." + provider + ".client-secret");
        return clientId;
    }

    public String getRedirectUri(String provider) {
        String redirectURI = env.getProperty("spring.security.oauth2.client.registration." + provider + ".redirect-uri");
        return redirectURI;
    }

    public String getUserInfoProviderURL(String provider) {
        String userInfoURL = env.getProperty("spring.security.oauth2.client.provider." + provider + ".user-info-uri");

        return userInfoURL;

    }
    public String getRedirectFront() {
        String userInfoURL = env.getProperty("spring.security.oauth2.client.redirect");

        return userInfoURL;

    }
}
