package beblue.io.repository;

import beblue.io.model.OrdersItems;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersItemsRepository extends JpaRepository<OrdersItems, Long> {

    @Query("SELECT ORD FROM OrdersItems ORD WHERE ORD.orders.id = ?1 ORDER BY ORD.orders.id ASC")
    List<OrdersItems> findByOrder(Long order_id);

}
