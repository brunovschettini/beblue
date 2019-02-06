package beblue.io.resource;

import beblue.io.helper.GenreHelper;
import beblue.io.model.Genre;
import beblue.io.repository.GenreRepository;
import java.util.List;
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
    public ResponseEntity<List<Genre>> genres() {

        GenreHelper genreHelper = new GenreHelper(genreRepository);

        return new ResponseEntity<List<Genre>>(genreHelper.loadGenre(), HttpStatus.OK);
    }

}
