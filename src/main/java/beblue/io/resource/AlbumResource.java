package beblue.io.resource;

import beblue.io.auth.SpotifyAuthorization;
import static beblue.io.auth.SpotifyAuthorization.spotifyApi;
import beblue.io.dao.AlbumDao;
import beblue.io.dao.ArtistDao;
import beblue.io.dao.GenreDao;
import beblue.io.model.Album;
import beblue.io.model.Artist;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/album/")
public class AlbumResource {

    private Map<Integer, Album> album;

    public AlbumResource() {
        album = new HashMap<Integer, Album>();

        Album a1 = new Album(1L, "2", "3", null, null, new Date());
        Album a2 = new Album(2L, "2", "3", null, null, new Date());
        Album a3 = new Album(3L, "2", "3", null, null, new Date());

        album.put(1, a1);
        album.put(2, a2);
        album.put(3, a3);
    }

    @RequestMapping(value = "/album", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> listar() {
        return new ResponseEntity<List<Album>>(new ArrayList<Album>(album.values()), HttpStatus.OK);
    }

    @RequestMapping(value = "/genre/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> genre(@PathVariable("name") String name) {
        return genre(name, 0);
    }

    @RequestMapping(value = "/genre/{name}/{offset}", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> genre(@PathVariable("name") String name, @PathVariable("offset") Integer offset) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (offset == null) {
            offset = 0;
        }
        GenreDao genreDao = new GenreDao();
        if (name.toLowerCase().equals("mbp") || name.toLowerCase().equals("rock") || name.toLowerCase().equals("pop") || name.toLowerCase().equals("classic")) {

        } else {
            if (genreDao.findByName(name) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            return new ResponseEntity(listAlbum, HttpStatus.OK);
        } catch (IOException | SpotifyWebApiException e) {
            notifyResponse.setObject("Error: " + e.getMessage());
            notifyResponse.setResult(0);
            notifyResponse.setStatus(0);
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity(e, HttpStatus.EXPECTATION_FAILED);
        }
    }
//
//    @GET
//    @Path("/spotify_id/{spotify_id}/")
//    @Produces({MediaType.APPLICATION_JSON})
//    public synchronized Response findBySpotifyId(@PathParam("spotify_id") String spotify_id) {
//        try {
//            if (spotify_id == null) {
//                NotifyResponse nr = new NotifyResponse();
//                nr.setStatus(0);
//                nr.setResult(0);
//                nr.setObject("Empty album id!");
//                return Response.status(200).entity(new Gson().toJson(nr)).build();
//            }
//            Album album = new AlbumDao().findBySpotifyId(spotify_id);
//            if (album == null) {
//                NotifyResponse nr = new NotifyResponse();
//                nr.setStatus(0);
//                nr.setResult(0);
//                nr.setObject("Album not found!");
//                return Response.status(200).entity(new Gson().toJson(nr)).build();
//            }
//            SearchAlbumsRequest search = spotifyApi.searchAlbums(spotify_id + "")
//                    .market(CountryCode.BR)
//                    .limit(50)
//                    .build();
//            Paging<AlbumSimplified> result = search.execute();
//            for (AlbumSimplified item : result.getItems()) {
//
//            }
//            return Response.status(200).entity(new Gson().toJson(result.getItems())).build();
//        } catch (IOException ex) {
//            Logger.getLogger(AlbumsApi.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SpotifyWebApiException ex) {
//            Logger.getLogger(AlbumsApi.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return Response.status(200).entity(new Gson().toJson("")).build();
//    }

}
