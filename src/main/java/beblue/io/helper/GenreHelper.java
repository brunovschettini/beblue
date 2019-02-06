package beblue.io.helper;

import beblue.io.auth.SpotifyAuthorization;
import static beblue.io.auth.SpotifyAuthorization.spotifyApi;
import beblue.io.model.Genre;
import beblue.io.repository.GenreRepository;
import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
 
public class GenreHelper {
    
    @Autowired
    GenreRepository genreRepository = null;
    
    public List<Genre> loadGenre() {
        
        List<Genre> listGenres = genreRepository.findAll();
        
        if (listGenres.isEmpty()) {
            SpotifyAuthorization.loadCredentialsSync();
            Gson gson = new Gson();
            NotifyResponse notifyResponse = new NotifyResponse();
            notifyResponse.setObject("OK");
            GetAvailableGenreSeedsRequest getAvailableGenreSeedsRequest = spotifyApi.getAvailableGenreSeeds().build();
            String[] strings;
            try {
                strings = getAvailableGenreSeedsRequest.execute();
                for (String string : strings) {
                    Genre g = new Genre();
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
