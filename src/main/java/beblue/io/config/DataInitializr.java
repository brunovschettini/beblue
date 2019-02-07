package beblue.io.config;

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
import beblue.io.model.Genre;
import beblue.io.model.Weeks;
import beblue.io.model.WeeksSale;
import beblue.io.repository.AlbumRepository;
import beblue.io.repository.ArtistRepository;
import beblue.io.repository.GenreRepository;
import beblue.io.repository.WeeksRepository;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import org.springframework.data.annotation.Persistent;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Persistent
    private EntityManager em;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private WeeksRepository weeksRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            SpotifyAuthorization.loadCredentialsSync();

            // ADICIONA SEMANAS
            WeeksHelper weeksHelper = new WeeksHelper(weeksRepository);
            weeksHelper.load();

            // ADICIONA PROMOÇÃO DA SEMANA + GÊNERO
            loadWeekesSales();

            GenreHelper genreHelper = new GenreHelper(genreRepository);
            genreHelper.loadGenre();
            AlbumHelper albumHelper = new AlbumHelper(genreRepository, albumRepository, artistRepository);
            albumHelper.storesSpotifyAlbums();
        } catch (IOException | SpotifyWebApiException ex) {
            Logger.getLogger(DataInitializr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadWeekesSales() {
        // sun
        storeWeekesSales(0, "pop", new BigDecimal(25));
        storeWeekesSales(0, "mpb", new BigDecimal(30));
        storeWeekesSales(0, "classic", new BigDecimal(35));
        storeWeekesSales(0, "rock", new BigDecimal(40));
        // mon
        storeWeekesSales(1, "pop", new BigDecimal(7));
        storeWeekesSales(1, "mpb", new BigDecimal(5));
        storeWeekesSales(1, "classic", new BigDecimal(3));
        storeWeekesSales(1, "rock", new BigDecimal(10));
        // tue
        storeWeekesSales(2, "pop", new BigDecimal(6));
        storeWeekesSales(2, "mpb", new BigDecimal(10));
        storeWeekesSales(2, "classic", new BigDecimal(5));
        storeWeekesSales(2, "rock", new BigDecimal(15));
        // wed
        storeWeekesSales(3, "pop", new BigDecimal(2));
        storeWeekesSales(3, "mpb", new BigDecimal(15));
        storeWeekesSales(3, "classic", new BigDecimal(8));
        storeWeekesSales(3, "rock", new BigDecimal(15));
        // thu
        storeWeekesSales(4, "pop", new BigDecimal(10));
        storeWeekesSales(4, "mpb", new BigDecimal(20));
        storeWeekesSales(4, "classic", new BigDecimal(13));
        storeWeekesSales(4, "rock", new BigDecimal(15));
        // fri
        storeWeekesSales(5, "pop", new BigDecimal(15));
        storeWeekesSales(5, "mpb", new BigDecimal(25));
        storeWeekesSales(5, "classic", new BigDecimal(18));
        storeWeekesSales(5, "rock", new BigDecimal(20));
        // sat
        storeWeekesSales(6, "pop", new BigDecimal(20));
        storeWeekesSales(6, "mpb", new BigDecimal(30));
        storeWeekesSales(6, "classic", new BigDecimal(25));
        storeWeekesSales(6, "rock", new BigDecimal(40));
    }

    private void storeWeekesSales(Integer weekday, String genre, BigDecimal percent) {
        Weeks w = weeksRepository.findByNumber_day(weekday);
        Genre g = genreRepository.findByName(genre);
        WeeksSale weeksSale = new WeeksSale(null, w, g, percent, new Date());
        em.persist(genre);
    }

}
