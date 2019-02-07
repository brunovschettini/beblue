package beblue.io.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import beblue.io.model.Album;
import beblue.io.model.Genre;
import beblue.io.repository.AlbumRepository;
import beblue.io.repository.ArtistRepository;
import beblue.io.repository.GenreRepository;
import beblue.io.utils.Result;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@RestController
public class AlbumResource {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @PersistenceContext
    EntityManager em;

    @RequestMapping(value = "/album/genre/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> genre(@PathVariable("name") String name) {
        return genre(name, 0);
    }

    @RequestMapping(value = "/album/genre/{name}/{offset}", method = RequestMethod.GET)
    public ResponseEntity<?> genre(@PathVariable("name") String name,
            @PathVariable("offset") Integer offset) {
        Result result = new Result();
        if (name == null || name.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty genre!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Genre genre = genreRepository.findByName(name);
        if (offset == null) {
            offset = 0;
        }

        if (name.toLowerCase().equals("mbp") || name.toLowerCase().equals("rock") || name.toLowerCase().equals("pop")
                || name.toLowerCase().equals("classic")) {

        } else {
            if (genre == null) {
                result.setStatus_code(0);
                result.setStatus("empty genre!");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        Query q = em.createNativeQuery("SELECT A.* FROM album A WHERE A.genre_id = " + genre.getId() + " ORDER BY A.name LIMIT 50 OFFSET " + offset, Album.class);
        List<Album> listAlbum = q.getResultList();
        if (listAlbum.isEmpty()) {
            result.setStatus("empty albums!");
        }
        return new ResponseEntity<>(listAlbum, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> id(@PathVariable("id") Long id) {
        Result result = new Result();
        if (id == null) {
            result.setStatus_code(0);
            result.setStatus("empty album id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Album a = albumRepository.findByCode(id);
        if (a == null) {
            result.setStatus_code(0);
            result.setStatus("album not found!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/spotify_id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> spotify_id(@PathVariable("spotify_id") String spotify_id) {
        Result result = new Result();
        if (spotify_id == null || spotify_id.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty spotify_id album!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Album a = albumRepository.findBySpotify_id(spotify_id);
        if (a == null) {
            result.setStatus_code(0);
            result.setStatus("album not found!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(a, HttpStatus.OK);
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
