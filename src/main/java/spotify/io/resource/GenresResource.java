package spotify.io.resource;

import spotify.io.helper.GenreHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spotify.io.repository.GenresRepository;

@RestController
// @RequestMapping(path="/api")
public class GenresResource {

    @Autowired
    GenresRepository genreRepository = null;

    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public ResponseEntity<?> genres() {
        GenreHelper genreHelper = new GenreHelper(genreRepository);
        return new ResponseEntity<>(genreHelper.loadGenre(), HttpStatus.OK);
    }

}
