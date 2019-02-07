package beblue.io.resource;

import beblue.io.helper.GenreHelper;
import beblue.io.repository.GenreRepository;
import beblue.io.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreResource {

    @Autowired
    GenreRepository genreRepository = null;

    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public ResponseEntity<?> genres() {
        GenreHelper genreHelper = new GenreHelper(genreRepository);
        return new ResponseEntity<>(genreHelper.loadGenre(), HttpStatus.OK);
    }

}
