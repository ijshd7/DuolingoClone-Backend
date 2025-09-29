package com.testingpractice.duoclonebackend.auth;

import com.testingpractice.duoclonebackend.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    public GoogleTokenResponse exchangeCodeForAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(
                "https://oauth2.googleapis.com/token",
                request,
                GoogleTokenResponse.class
        );

    }

    public GoogleUserInfo fetchUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        return restTemplate.getForObject(url, GoogleUserInfo.class);
    }

}