package beblue.io.helper;

import static beblue.io.auth.SpotifyAuthorization.spotifyApi;
import beblue.io.model.Genre;
import beblue.io.repository.GenreRepository;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
@Component
public class GenreHelper {

    private GenreRepository genreRepository;

    public GenreHelper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> loadGenre() {

        List<Genre> listGenres = genreRepository.findAll();

        if (listGenres.isEmpty()) {

            GetAvailableGenreSeedsRequest getAvailableGenreSeedsRequest = spotifyApi.getAvailableGenreSeeds().build();
            String[] strings;
            try {
                strings = getAvailableGenreSeedsRequest.execute();
                Genre g = new Genre();
                g.setName("classic");
                genreRepository.save(g);
                for (String string : strings) {
                    g = new Genre();
                    g.setName(string);
                    genreRepository.save(g);
                }
                listGenres = genreRepository.findAll();
            } catch (IOException | SpotifyWebApiException ex) {
                Logger.getLogger(GenreHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listGenres;
    }

}
