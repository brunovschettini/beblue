package beblue.io.resource;

import beblue.io.auth.SpotifyAuthorization;
import static beblue.io.auth.SpotifyAuthorization.spotifyApi;
import beblue.io.dao.GenreDao;
import beblue.io.model.Genre;
import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreResource {

    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public ResponseEntity<List<Genre>> genres() {

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
                Logger.getLogger(GenreResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ResponseEntity(listGenres, HttpStatus.OK);
    }

}
