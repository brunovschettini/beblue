package beblue.io.dao;

import beblue.io.model.Genre;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GenreDao {

    @PersistenceContext
    private EntityManager em;

    public List<Genre> findAll() {
        Query query = em.createQuery("SELECT G FROM Genre G ORDER BY G.name ASC");
        return query.getResultList();
    }

    public Genre findByName(String name) {
        Query query = em.createQuery("SELECT G FROM Genre G WHERE G.name = :name");
        query.setParameter("name", name);
        if (!query.getResultList().isEmpty()) {
            return (Genre) query.getSingleResult();
        }
        return null;
    }

    public void store(String name) {
        if (findByName(name) == null) {
            em.getTransaction().begin();
            try {
                Genre genre = new Genre();
                genre.setName(name);
                em.persist(genre);
                em.flush();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();

            }

        }

    }

}
