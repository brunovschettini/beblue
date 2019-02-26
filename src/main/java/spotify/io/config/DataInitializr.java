package spotify.io.config;

import spotify.io.helper.WeeksSalesHelper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import spotify.io.auth.SpotifyAuthorization;
import spotify.io.helper.AlbumHelper;
import spotify.io.helper.GenreHelper;
import spotify.io.helper.WeeksHelper;
import spotify.io.model.Users;
import spotify.io.repository.WeeksRepository;
import spotify.io.repository.WeeksSaleRepository;
import spotify.io.repository.AlbumsRepository;
import spotify.io.repository.ArtistsRepository;
import spotify.io.repository.GenresRepository;
import spotify.io.repository.UsersRepository;
import java.util.Date;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private GenresRepository genreRepository;

    @Autowired
    private AlbumsRepository albumRepository;

    @Autowired
    private ArtistsRepository artistRepository;

    @Autowired
    private WeeksRepository weeksRepository;

    @Autowired
    private WeeksSaleRepository weeksSaleRepository;

    @Autowired
    private UsersRepository usersRepository;

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
            new WeeksSalesHelper(genreRepository, weeksRepository, weeksSaleRepository).loadWeekesSales();

            Users u = new Users(null, "admin", "admin", new Date());
            usersRepository.save(u);

            AlbumHelper albumHelper = new AlbumHelper(genreRepository, albumRepository, artistRepository);
            albumHelper.storesSpotifyAlbums();
        } catch (IOException | SpotifyWebApiException ex) {
            Logger.getLogger(DataInitializr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
