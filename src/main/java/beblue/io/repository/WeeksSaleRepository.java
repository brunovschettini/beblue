package beblue.io.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeksSaleRepository extends JpaRepository<WeeksRepository, Long> {

    

}
