package beblue.io.config;

import beblue.io.helper.WeeksSaleHelper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import beblue.io.auth.SpotifyAuthorization;
import beblue.io.helper.AlbumHelper;
import beblue.io.helper.GenreHelper;
import beblue.io.helper.WeeksHelper;
import beblue.io.repository.AlbumRepository;
import beblue.io.repository.ArtistRepository;
import beblue.io.repository.GenreRepository;
import beblue.io.repository.WeeksRepository;
import beblue.io.repository.WeeksSaleRepository;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private WeeksRepository weeksRepository;

    @Autowired
    private WeeksSaleRepository weeksSaleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            SpotifyAuthorization.loadCredentialsSync();

            // Add weeks
            WeeksHelper weeksHelper = new WeeksHelper(weeksRepository);
            weeksHelper.load();

            // Add genre
            GenreHelper genreHelper = new GenreHelper(genreRepository);
            genreHelper.loadGenre();

            // Add sales week
            new WeeksSaleHelper(genreRepository, weeksRepository, weeksSaleRepository).loadWeekesSales();

            AlbumHelper albumHelper = new AlbumHelper(genreRepository, albumRepository, artistRepository);
            albumHelper.storesSpotifyAlbums();
        } catch (IOException | SpotifyWebApiException ex) {
            Logger.getLogger(DataInitializr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
