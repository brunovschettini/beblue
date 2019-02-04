package br.com.beblue.apis;

import br.com.beblue.auth.SpotifyAuthorization;
import static br.com.beblue.auth.SpotifyAuthorization.spotifyApi;
import br.com.beblue.dao.ArtistDao;
import br.com.beblue.dao.GenreDao;
import br.com.beblue.entity.Album;
import br.com.beblue.entity.Artist;
import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

// https://github.com/thelinmichael/spotify-web-api-java
@Path("/albums")
public class AlbumsApi {

    @Context
    HttpHeaders headers;

    @GET
    @Path("/genre/{name}/{offset}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response genre(@PathParam("name") String name, @PathParam("offset") Integer offset) {

        GenreDao genreDao = new GenreDao();

        if (genreDao.findByName(name) == null) {
            NotifyResponse nr = new NotifyResponse();
            nr.setStatus(0);
            nr.setResult(0);
            nr.setObject("Genre not found");
            return Response.status(200).entity(nr).build();
        }

        SpotifyAuthorization.loadCredentialsSync();
        Gson gson = new Gson();
        NotifyResponse notifyResponse = new NotifyResponse();
        notifyResponse.setObject("OK");

        SearchItemRequest searchItemRequest = spotifyApi.searchItem(name, ModelObjectType.ALBUM.getType())
                .market(CountryCode.BR)
                .offset(offset)
                .limit(50)
                .build();

        try {

            SearchResult searchResult = searchItemRequest.execute();

            Paging<AlbumSimplified> result = searchResult.getAlbums();

            List list = new ArrayList();

            for (int i = 0; i < result.getItems().length; i++) {

                ArtistSimplified[] as = result.getItems()[i].getArtists();

                JSONObject jsonObject = new JSONObject();

                Artist artist = new Artist();

                ArtistDao artistDao = new ArtistDao();

                artist = artistDao.store(as[0].getName(), as[0].getId());

                if (artist != null) {

                    Album album = new Album();

                    album.setSpotify_id(result.getItems()[i].getId());

                    album.setGenre(genreDao.findByName(name));

                    album.setArtist(artist);

                    album.setPrice(new BigDecimal("1,00"));

                }

                jsonObject.put("id", result.getItems()[i].getId());
                jsonObject.put("name", result.getItems()[i].getName());
                jsonObject.put("type", result.getItems()[i].getType());
                // jsonObject.put("images", listAlbums.getItems()[i].getImages());
                list.add(jsonObject);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", list.toString());
            return Response.status(200).entity(jsonObject.toString()).build();

            // System.out.println("Total: " + listAlbums.getTotal());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return Response.status(200).entity(gson.toJson(notifyResponse)).build();
    }

}
