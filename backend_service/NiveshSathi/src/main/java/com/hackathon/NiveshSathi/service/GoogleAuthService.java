package com.hackathon.NiveshSathi.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    public GoogleIdToken.Payload verify(String idTokenString) {
        System.out.println("Google Client ID used = " + googleClientId);

        try {
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance()
                    )
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token");
            }

            return idToken.getPayload();

        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed", e);
        }
    }
}
