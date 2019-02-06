package beblue.io.repository;

import beblue.io.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long> {
//
//    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser
//    Album findByName(String name);
//
//    Album findByEmail(String email);
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
