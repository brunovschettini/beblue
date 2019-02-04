package br.com.beblue.dao;

import br.com.beblue.conn.Conn;
import br.com.beblue.entity.Genre;
import java.util.List;
import javax.persistence.Query;

public class GenreDao extends Conn {

    public List<Genre> findAll() {
        Query query = getEntityManager().createQuery("SELECT G FROM Genre G ORDER BY G.name ASC");
        return query.getResultList();
    }

    public Genre findByName(String name) {
        Query query = getEntityManager().createQuery("SELECT G FROM Genre G WHERE G.name = :name");
        query.setParameter("name", name);
        if (!query.getResultList().isEmpty()) {
            return (Genre) query.getSingleResult();
        }
        return null;
    }

    public void store(String name) {
        if (findByName(name) == null) {
            getEntityManager().getTransaction().begin();
            try {
                Genre genre = new Genre();
                genre.setName(name);
                getEntityManager().persist(genre);
                getEntityManager().flush();
                getEntityManager().getTransaction().commit();
            } catch (Exception e) {
                getEntityManager().getTransaction().rollback();

            }

        }

    }

}
