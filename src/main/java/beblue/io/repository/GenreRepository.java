package beblue.io.repository;

import beblue.io.model.Genre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Override
    List<Genre> findAll();

    Genre findByName(String name);

}
