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
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@RestController
public class OrderResource {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @PersistenceContext
    EntityManager em;

    @RequestMapping(value = "/album/genre/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> genre(@PathVariable("name") String name) {
        return genre(name, 0);
    }

    @RequestMapping(value = "/album/genre/{name}/{offset}", method = RequestMethod.GET)
    public ResponseEntity<List<Album>> genre(@PathVariable("name") String name,
            @PathVariable("offset") Integer offset) {
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<List<Album>>(new ArrayList<Album>(), HttpStatus.OK);
        }
        Genre genre = genreRepository.findByName(name);
        if (offset == null) {
            offset = 0;
        }

        if (name.toLowerCase().equals("mbp") || name.toLowerCase().equals("rock") || name.toLowerCase().equals("pop")
                || name.toLowerCase().equals("classic")) {

        } else {
            if (genre == null) {
                return new ResponseEntity<List<Album>>(new ArrayList<Album>(), HttpStatus.OK);
            }
        }
        Query q = em.createNativeQuery("SELECT A.* FROM album A WHERE A.genre_id = " + genre.getId() + " ORDER BY A.name LIMIT 50 OFFSET " + offset, Album.class);
        List<Album> listAlbum = q.getResultList();
        return new ResponseEntity<List<Album>>(listAlbum, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Album> spotify_id(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<Album>(new Album(), HttpStatus.OK);
        }
        Album a = albumRepository.findByCode(id);
        if (a == null) {

            return new ResponseEntity<Album>(new Album(), HttpStatus.OK);
        }
        return new ResponseEntity<Album>(a, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/spotify_id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Album> spotify_id(@PathVariable("spotify_id") String spotify_id) {
        if (spotify_id == null || spotify_id.isEmpty()) {
            return new ResponseEntity<Album>(new Album(), HttpStatus.OK);
        }
        Album a = albumRepository.findBySpotify_id(spotify_id);
        if (a == null) {
            return new ResponseEntity<Album>(new Album(), HttpStatus.OK);
        }
        return new ResponseEntity<Album>(a, HttpStatus.OK);
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
