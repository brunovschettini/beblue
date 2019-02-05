package br.com.beblue.api;

import br.com.beblue.auth.SpotifyAuthorization;
import static br.com.beblue.auth.SpotifyAuthorization.spotifyApi;
import br.com.beblue.dao.AlbumDao;
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
import com.wrapper.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/albums")
public class AlbumsApi {

    @Context
    HttpHeaders headers;

    @GET
    @Path("/genre/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response genre(@PathParam("name") String name) {
        return genre(name, 0);
    }

    @GET
    @Path("/genre/{name}/{offset}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response genre(@PathParam("name") String name, @PathParam("offset") Integer offset) {
        if (name == null || name.isEmpty()) {
            NotifyResponse nr = new NotifyResponse();
            nr.setStatus(0);
            nr.setResult(0);
            nr.setObject("Empty genre!");
            return Response.status(200).entity(new Gson().toJson(nr)).build();
        }
        if (offset == null) {
            offset = 0;
        }
        GenreDao genreDao = new GenreDao();
        if (name.toLowerCase().equals("mbp") || name.toLowerCase().equals("rock") || name.toLowerCase().equals("pop") || name.toLowerCase().equals("classic")) {

        } else {
            if (genreDao.findByName(name) == null) {
                NotifyResponse nr = new NotifyResponse();
                nr.setStatus(0);
                nr.setResult(0);
                nr.setObject("Genre not found!");
                return Response.status(200).entity(new Gson().toJson(nr)).build();
            }
        }
        Gson gson = new Gson();
        NotifyResponse notifyResponse = new NotifyResponse();
        notifyResponse.setObject("OK");

        try {
            AlbumDao albumDao = new AlbumDao();
            List<Album> listAlbum = albumDao.findByGenre(name);
            if (listAlbum.isEmpty()) {
                SpotifyAuthorization.loadCredentialsSync();
                SearchItemRequest searchItemRequest = spotifyApi.searchItem(name, ModelObjectType.ALBUM.getType())
                        .market(CountryCode.BR)
                        .offset(offset)
                        .limit(50)
                        .build();
                SearchResult searchResult = searchItemRequest.execute();
                if (searchResult != null && searchResult.getAlbums().getTotal() > 0) {
                    Paging<AlbumSimplified> result = searchResult.getAlbums();

                    for (AlbumSimplified item : result.getItems()) {
                        ArtistSimplified[] as = item.getArtists();
                        ArtistDao artistDao = new ArtistDao();
                        Artist artist = artistDao.store(as[0].getName(), as[0].getId());
                        if (artist != null) {
                            Album album = new Album();
                            if (item.getName().isEmpty()) {
                                album.setName(as[0].getName());
                            } else {
                                album.setName(item.getName());
                            }
                            album.setSpotify_id(item.getId());
                            album.setGenre(genreDao.findByName(name));
                            album.setArtist(artist);
                            album.setPrice(new BigDecimal("1.00"));
                            albumDao.store(album);
                        }
                    }
                }
            }
            listAlbum = albumDao.findByGenre(name);
            return Response.status(200).entity(new Gson().toJson(listAlbum)).build();
        } catch (IOException | SpotifyWebApiException e) {
            notifyResponse.setObject("Error: " + e.getMessage());
            notifyResponse.setResult(0);
            notifyResponse.setStatus(0);
            System.out.println("Error: " + e.getMessage());
        }

        return Response.status(200).entity(gson.toJson(notifyResponse)).build();
    }

    @GET
    @Path("/{spotify_id}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response genre(@PathParam("spotify_id") String spotify_id) {
        try {
            if (spotify_id == null) {
                NotifyResponse nr = new NotifyResponse();
                nr.setStatus(0);
                nr.setResult(0);
                nr.setObject("Empty album id!");
                return Response.status(200).entity(new Gson().toJson(nr)).build();
            }
            Album album = new AlbumDao().findBySpotifyId(spotify_id);
            if (album == null) {
                NotifyResponse nr = new NotifyResponse();
                nr.setStatus(0);
                nr.setResult(0);
                nr.setObject("Album not found!");
                return Response.status(200).entity(new Gson().toJson(nr)).build();
            }
            SearchAlbumsRequest search = spotifyApi.searchAlbums(spotify_id + "")
                    .market(CountryCode.BR)
                    .limit(50)
                    .build();
            Paging<AlbumSimplified> result = search.execute();
            for (AlbumSimplified item : result.getItems()) {

            }
            return Response.status(200).entity(new Gson().toJson(result.getItems())).build();
        } catch (IOException ex) {
            Logger.getLogger(AlbumsApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SpotifyWebApiException ex) {
            Logger.getLogger(AlbumsApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(new Gson().toJson("")).build();
    }

}
