package spotify.io.repository;

import spotify.io.model.Genres;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

    @Override
    List<Genres> findAll();

    Genres findByName(String name);

}
