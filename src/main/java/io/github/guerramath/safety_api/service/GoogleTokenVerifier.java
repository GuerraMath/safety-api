package io.github.guerramath.safety_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servico para verificar tokens de ID do Google.
 */
@Service
public class GoogleTokenVerifier {

    @Value("${google.client-id:727557341501-t7jdlqukns4etesst2400shndiqdsji8.apps.googleusercontent.com}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    /**
     * Verifica e decodifica um Google ID Token.
     *
     * @param idTokenString O token JWT do Google
     * @return GoogleIdToken.Payload com as informacoes do usuario, ou null se invalido
     */
    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            if (verifier == null) {
                verifier = new GoogleIdTokenVerifier.Builder(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance())
                        .setAudience(Collections.singletonList(googleClientId))
                        .build();
            }

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
