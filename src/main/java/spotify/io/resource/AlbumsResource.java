package spotify.io.resource;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import spotify.io.model.Albums;
import spotify.io.model.Genres;
import spotify.io.utils.Result;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import spotify.io.repository.AlbumsRepository;
import spotify.io.repository.ArtistsRepository;
import spotify.io.repository.GenresRepository;

@RestController
// @RequestMapping(path="/api")
public class AlbumsResource {

    @Autowired
    GenresRepository genreRepository;

    @Autowired
    AlbumsRepository albumRepository;

    @Autowired
    ArtistsRepository artistRepository;

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
        Genres genre = genreRepository.findByName(name);
        if (genre == null) {
            result.setStatus_code(0);
            result.setStatus("empty genre!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        if (offset == null) {
            offset = 0;
        }
        if (name.toLowerCase().equals("mpb") || name.toLowerCase().equals("rock") || name.toLowerCase().equals("pop") || name.toLowerCase().equals("classic")) {
            // PageRequest pageRequest = PageRequest.of(offset, 50, Sort.by(Sort.Direction.ASC, "name"));
            // Page<Albums> listAlbum = albumRepository.findAll(pageRequest);
            Query q = em.createNativeQuery("SELECT A.* FROM albums A WHERE A.genre_id = " + genre.getId() + " ORDER BY A.name LIMIT 50 OFFSET " + offset, Albums.class);
            List<Albums> listAlbum = q.getResultList();
            if (listAlbum.isEmpty()) {
                result.setStatus("empty albums!");
            }
            return new ResponseEntity<>(listAlbum, HttpStatus.OK);
        }
        result.setStatus_code(0);
        result.setStatus("empty albums!");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> id(@PathVariable("id") Long id) {
        Result result = new Result();
        if (id == null) {
            result.setStatus_code(0);
            result.setStatus("empty album id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Albums a = albumRepository.findByCode(id);
        if (a == null) {
            result.setStatus_code(0);
            result.setStatus("album not found!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/spotify_id/{spotify_id}", method = RequestMethod.GET)
    public ResponseEntity<?> spotify_id(@PathVariable("spotify_id") String spotify_id) {
        Result result = new Result();
        if (spotify_id == null || spotify_id.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty spotify_id album!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Albums a = albumRepository.findBySpotify_id(spotify_id);
        if (a == null) {
            result.setStatus_code(0);
            result.setStatus("album not found!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(a, HttpStatus.OK);
    }
}
