package beblue.io.helper;

import static beblue.io.auth.SpotifyAuthorization.spotifyApi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.wrapper.spotify.enums.ModelObjectType;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.data.search.SearchItemRequest;

import beblue.io.model.Albums;
import beblue.io.model.Artists;
import beblue.io.model.Genres;
import com.neovisionaries.i18n.CountryCode;
import java.util.Random;
import org.springframework.stereotype.Component;
import beblue.io.repository.AlbumsRepository;
import beblue.io.repository.ArtistsRepository;
import beblue.io.repository.GenresRepository;

@SuppressWarnings("unchecked")
@Component
public class AlbumHelper {

    public GenresRepository genreRepository;
    public AlbumsRepository albumRepository;
    public ArtistsRepository artistRepository;

    public AlbumHelper(GenresRepository genreRepository, AlbumsRepository albumRepository,
            ArtistsRepository artistRepository) {
        this.genreRepository = genreRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public void storesSpotifyAlbums() throws IOException, SpotifyWebApiException {

        List<String> listGenreString = new ArrayList<String>();
        listGenreString.add("mpb");
        listGenreString.add("classic");
        listGenreString.add("pop");
        listGenreString.add("rock");
        for (String genreString : listGenreString) {
            Genres genre = genreRepository.findByName(genreString);
            List<SearchResult> listSearchResult = new ArrayList<>();
            int z = 19;
            while (z != 20) {
                SearchItemRequest searchItemRequest = spotifyApi.searchItem(genreString, ModelObjectType.ALBUM.getType()).limit(50).offset(z).market(CountryCode.BR).build();
                SearchResult searchResults = searchItemRequest.execute();
                if (searchResults.getAlbums().getTotal() > 0) {
                    listSearchResult.add(searchResults);
                    z++;
                } else {
                    z = 20;
                }
            }
            int count = 0;
            for (SearchResult searchResult : listSearchResult) {

                if (searchResult != null && searchResult.getAlbums().getTotal() > 0) {
                    Paging<AlbumSimplified> result = searchResult.getAlbums();

                    for (AlbumSimplified item : result.getItems()) {
                        ArtistSimplified[] as = item.getArtists();
                        Artists artist = artistRepository.findByNameAndSpotify_id(as[0].getName(), as[0].getId());
                        System.err.println(count);
                        count++;
                        if (artist == null) {
                            artist = new Artists();
                            artist.setName(as[0].getName());
                            artist.setSpotify_id(as[0].getId());
                            artist = artistRepository.save(artist);
                        }
                        if (artist != null) {
                            Albums album = new Albums();
                            if (albumRepository.findBySpotify_id(item.getId()) == null) {
                                if (item.getName().isEmpty()) {
                                    album.setName(as[0].getName());
                                } else {
                                    album.setName(item.getName());
                                }
                                album.setSpotify_id(item.getId());
                                album.setGenre(genre);
                                album.setArtist(artist);
                                Random random = new Random();
                                random.doubles(10L, 100L);
                                double d = random.nextDouble() * 100;
                                album.setPrice(new BigDecimal(d));
                                albumRepository.save(album);
                            }
                        }
                    }
                }
            }

        }

    }
}
