package spotify.io.repository;

import spotify.io.model.Weeks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeksRepository extends JpaRepository<Weeks, Long> {

    @Query("SELECT W FROM Weeks W WHERE W.number_day = ?1")
    Weeks findByNumber_day(Integer number_day);

}
