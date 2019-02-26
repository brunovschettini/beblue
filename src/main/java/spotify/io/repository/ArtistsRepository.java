package spotify.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import spotify.io.model.Artists;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistsRepository extends JpaRepository<Artists, Long> {

    @Query("SELECT A FROM Artists A WHERE A.name = ?1 AND A.spotify_id = ?2")
    Artists findByNameAndSpotify_id(String name, String spotify_id);

}
