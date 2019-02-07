package beblue.io.repository;

import beblue.io.model.WeeksSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeksSaleRepository extends JpaRepository<WeeksSales, Long> {

    @Override
    WeeksSales getOne(Long id);

}
