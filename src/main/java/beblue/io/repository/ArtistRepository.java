package beblue.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import beblue.io.model.Artist;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("SELECT A FROM Artist A WHERE A.name = ?1 AND A.spotify_id = ?2")
    Artist findByNameAndSpotify_id(String name, String spotify_id);

}
