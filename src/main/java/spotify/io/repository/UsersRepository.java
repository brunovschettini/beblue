package spotify.io.repository;

import spotify.io.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser com a query
    @Query("SELECT U FROM Users U WHERE U.name = ?1")
    Users findByNameAnywhere(String name);

    // Com essa médoto consigo realizar queries pelo parâmetro que eu quiser com a query
    @Query("SELECT U FROM Users U WHERE U.name LIKE %?1%")
    Users findByNameContains(String name);

    // IgnoreCase
    Users findByNameIgnoreCase(String name);
}
