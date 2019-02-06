package beblue.io.repository;

import beblue.io.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
//
//    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser
//    Album findByName(String name);
//
    @Query("SELECT A FROM Album A WHERE A.id = ?1")
    Order findByCode(Long id);

    @Query("SELECT A FROM Album A WHERE A.spotify_id = ?1")
    Order findBySpotify_id(String spotify_id);

//
//    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser com a query
//    @Query("SELECT U FROM User U WHERE U.name = ?1")
//    Album findByNameAnywhere(String name);
//
//    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser com a query
//    @Query("SELECT U FROM User U WHERE U.name LIKE %?1%")
//    Album findByNameContains(String name);
//
//    // IgnoreCase
//    Album findByNameIgnoreCase(String name);

}
