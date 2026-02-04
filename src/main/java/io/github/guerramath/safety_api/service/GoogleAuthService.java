package io.github.guerramath.safety_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;


import java.util.Collections;
import org.springframework.stereotype.Service;


@Service
public class GoogleAuthService {

    // Substitua pelo seu Client ID gerado no Google Cloud Console
    private static final String CLIENT_ID = "727557341501-t7jdlqukns4etesst2400shndiqdsji8.apps.googleusercontent.com";

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new RuntimeException("Token do Google inválido");
        }
    }
}