package spotify.io.repository;

import spotify.io.model.OrdersItems;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersItemsRepository extends JpaRepository<OrdersItems, Long> {

    @Query("SELECT ORD FROM OrdersItems ORD WHERE ORD.order.id = ?1 ORDER BY ORD.order.id ASC")
    List<OrdersItems> findByOrder(Long order_id);

    @Query("SELECT ORD FROM OrdersItems ORD WHERE ORD.created_at >= ?1 AND ORD.created_at <= ?2 ORDER BY ORD.created_at ASC, ORD.order.id ASC, ORD.album.name ASC")
    List<OrdersItems> findByDates(Date start_date, Date end_date);

    @Query("SELECT ORD FROM OrdersItems ORD ORDER BY ORD.created_at ASC, ORD.order.id ASC, ORD.album.name ASC")
    List<OrdersItems> findByAll();

}
