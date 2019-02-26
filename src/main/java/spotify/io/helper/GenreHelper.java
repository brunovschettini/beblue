package spotify.io.helper;

import static spotify.io.auth.SpotifyAuthorization.spotifyApi;
import spotify.io.model.Genres;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import spotify.io.repository.GenresRepository;

@SuppressWarnings("unchecked")
@Component
public class GenreHelper {

    private GenresRepository genreRepository;

    public GenreHelper(GenresRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genres> loadGenre() {

        List<Genres> listGenres = genreRepository.findAll();

        if (listGenres.isEmpty()) {

            GetAvailableGenreSeedsRequest getAvailableGenreSeedsRequest = spotifyApi.getAvailableGenreSeeds().build();
            String[] strings;
            try {
                strings = getAvailableGenreSeedsRequest.execute();
                Genres g = new Genres();
                g.setName("classic");
                genreRepository.save(g);
                for (String string : strings) {
                    g = new Genres();
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
