package br.com.beblue.apis;

import br.com.beblue.auth.SpotifyAuthorization;
import static br.com.beblue.auth.SpotifyAuthorization.spotifyApi;
import br.com.beblue.dao.GenreDao;
import br.com.beblue.entity.Genre;
import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/genres")
public class GenresApi {

    @Context
    HttpHeaders headers;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response genres() {

        GenreDao genreDao = new GenreDao();

        List<Genre> listGenres = genreDao.findAll();

        if (listGenres.isEmpty()) {
            SpotifyAuthorization.loadCredentialsSync();
            Gson gson = new Gson();
            NotifyResponse notifyResponse = new NotifyResponse();
            notifyResponse.setObject("OK");
            GetAvailableGenreSeedsRequest getAvailableGenreSeedsRequest = spotifyApi.getAvailableGenreSeeds().build();
            String[] strings;
            try {
                strings = getAvailableGenreSeedsRequest.execute();
                for (int i = 0; i < strings.length; i++) {
                    genreDao.store(strings[i]);
                }
                listGenres = genreDao.findAll();
            } catch (IOException | SpotifyWebApiException ex) {
                Logger.getLogger(GenresApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
        }

        JSONObject jSONObject = new JSONObject();
        jSONObject.put("genres", listGenres);
        return Response.status(200).entity(jSONObject.toString()).build();
    }

}
