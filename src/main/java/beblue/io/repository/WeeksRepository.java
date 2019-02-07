package beblue.io.repository;

import beblue.io.model.Weeks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeksRepository extends CrudRepository<Weeks, Long> {

    @Query("SELECT W FROM Weeks W WHERE W.number_day = ?1")
    Weeks findByNumber_day(Integer number_day);

}
