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
import beblue.io.model.Users;
import beblue.io.repository.WeeksRepository;
import beblue.io.repository.WeeksSaleRepository;
import beblue.io.repository.AlbumsRepository;
import beblue.io.repository.ArtistsRepository;
import beblue.io.repository.GenresRepository;
import beblue.io.repository.UsersRepository;
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
            new WeeksSaleHelper(genreRepository, weeksRepository, weeksSaleRepository).loadWeekesSales();
            
            Users u = new Users(null, "admin", "admin", new Date());
            usersRepository.save(u);

            AlbumHelper albumHelper = new AlbumHelper(genreRepository, albumRepository, artistRepository);
            albumHelper.storesSpotifyAlbums();
        } catch (IOException | SpotifyWebApiException ex) {
            Logger.getLogger(DataInitializr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
