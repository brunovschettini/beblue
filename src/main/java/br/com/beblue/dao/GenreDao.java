package br.com.beblue.ws;

import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import com.wrapper.spotify.SpotifyApi;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// https://github.com/thelinmichael/spotify-web-api-java
@Path("/disks")
public class DisksWS {

    @Context
    HttpHeaders headers;

    @GET
    @Path("/genre/{genre}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response importCities(@PathParam("genre") String genre) {
        Gson gson = new Gson();
        NotifyResponse notifyResponse = new NotifyResponse();
        notifyResponse.setObject("OK");

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("0a487071817e4899813ed05116175fa3")
                .setClientSecret("fb100b74e7bd43cca5dc4d62872ce62b")
                // .setRedirectUri("<your_redirect_uri>")
                .build();
        
        String token = spotifyApi.getAccessToken();
        
        

        return Response.status(200).entity(gson.toJson(notifyResponse)).build();
    }

}
