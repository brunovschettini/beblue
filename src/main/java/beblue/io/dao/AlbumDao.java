//ackage beblue.io.dao;
//
//import beblue.io.model.Album;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class AlbumDao {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public List<Album> findByGenre(String genre) {
//        Query query = em.createQuery("SELECT A FROM Album A WHERE A.genre.name = :genre ORDER BY A.name ASC");
//        query.setParameter("genre", genre);
//        query.setMaxResults(50);
//        return query.getResultList();
//    }
//
//    public Album findBySpotifyId(String spotify_id) {
//        Query query = em.createQuery("SELECT A FROM Album A WHERE A.spotify_id = :spotify_id ORDER BY A.name ASC");
//        query.setParameter("spotify_id", spotify_id);
//        return (Album) query.getSingleResult();
//    }
//
//    public void store(Album album) {
//        em.getTransaction().begin();
//        try {
//            em.persist(album);
//            em.flush();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            em.getTransaction().rollback();
//
//        }
//    }
//
//}
