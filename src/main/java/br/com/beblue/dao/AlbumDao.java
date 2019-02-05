package br.com.beblue.dao;

import br.com.beblue.conn.Conn;
import br.com.beblue.entity.Album;
import java.util.List;
import javax.persistence.Query;

public class AlbumDao extends Conn {

    public List<Album> findByGenre(String genre) {
        Query query = getEntityManager().createQuery("SELECT A FROM Album A WHERE A.genre.name = :genre ORDER BY A.name ASC");
        query.setParameter("genre", genre);
        query.setMaxResults(50);
        return query.getResultList();
    }

    public Album findBySpotifyId(String spotify_id) {
        Query query = getEntityManager().createQuery("SELECT A FROM Album A WHERE A.spotify_id = :spotify_id ORDER BY A.name ASC");
        query.setParameter("spotify_id", spotify_id);
        return (Album) query.getSingleResult();
    }

    public void store(Album album) {
        getEntityManager().getTransaction().begin();
        try {
            getEntityManager().persist(album);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();

        }
    }

}
