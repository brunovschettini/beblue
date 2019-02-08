package beblue.io.repository;

import beblue.io.model.Albums;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumsRepository extends JpaRepository<Albums, Long> {
//
//    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser
//    Album findByName(String name);
//

    @Query("SELECT A FROM Albums A WHERE A.id = ?1")
    Albums findByCode(Long id);

    @Query("SELECT A FROM Albums A WHERE A.spotify_id = ?1")
    Albums findBySpotify_id(String spotify_id);

}
