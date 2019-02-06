package beblue.io.config;

import beblue.io.auth.SpotifyAuthorization;
import beblue.io.helper.GenreHelper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        SpotifyAuthorization.loadCredentialsSync();
        GenreHelper genreHelper = new GenreHelper();
        genreHelper.loadGenre();
        
    }

}
