package spotify.io.auth;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpotifyAuthorization {

    public static String CLIENT_ID = "0a487071817e4899813ed05116175fa3";
    public static String CLIENT_SECRET = "fb100b74e7bd43cca5dc4d62872ce62b";
    // private static final URI redirectUri = SpotifyHttpManager.makeUri("https://example.com/spotify-redirect");
    private static final String CODE = "";
    public static String TOKEN = null;

    private static final SpotifyApi spotifyApiAuth = new SpotifyApi.Builder().setClientId(CLIENT_ID).setClientSecret(CLIENT_SECRET).build();

    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApiAuth.clientCredentials().build();

    public static SpotifyApi spotifyApi = null;

    public static void main(String... args) {
        loadCredentialsSync();
        // clientCredentials_Async();
    }

    public static void loadCredentialsSync() {
        if (TOKEN == null || TOKEN.isEmpty()) {
            try {
                final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

                // Set access token for further "spotifyApi" object usage
                spotifyApiAuth.setAccessToken(clientCredentials.getAccessToken());

                System.out.println("Expires in: " + clientCredentials.getExpiresIn());
                if (clientCredentials.getAccessToken() != null) {
                    TOKEN = clientCredentials.getAccessToken();
                    spotifyApi = new SpotifyApi.Builder().setAccessToken(TOKEN).build();
                } else {
                    System.out.println("Error: Returned empty token");
                }
            } catch (IOException | SpotifyWebApiException e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }

    public static void clientCredentials_Async() {
        try {
            final Future<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

            // ...
            final ClientCredentials clientCredentials = clientCredentialsFuture.get();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
            TOKEN = clientCredentials.getAccessToken();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
    }

}
