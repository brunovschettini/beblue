package br.com.beblue.dao;

import br.com.beblue.conn.Conn;
import br.com.beblue.entity.Artist;
import java.util.List;
import javax.persistence.Query;

public class ArtistDao extends Conn {

    public List<Artist> findAll() {
        Query query = getEntityManager().createQuery("SELECT A FROM Artist A ORDER BY A.name ASC");
        return query.getResultList();
    }

    public Artist findBySpotifyId(String spotify_id) {
        Query query = getEntityManager().createQuery("SELECT A FROM Artist A WHERE A.spotify_id = :spotify_id");
        query.setParameter("spotify_id", spotify_id);
        if (!query.getResultList().isEmpty()) {
            return (Artist) query.getSingleResult();
        }
        return null;
    }

    public Artist store(String name, String spotify_id) {
        Artist artist = findBySpotifyId(spotify_id);
        if (artist == null) {
            getEntityManager().getTransaction().begin();
            try {
                artist = new Artist();
                artist.setName(name);
                artist.setSpotify_id(spotify_id);
                getEntityManager().persist(artist);
                getEntityManager().flush();
                getEntityManager().getTransaction().commit();
            } catch (Exception e) {
                artist = null;
                getEntityManager().getTransaction().rollback();

            }
        }
        return artist;

    }

}
