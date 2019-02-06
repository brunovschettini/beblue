//package beblue.io.dao;
//
//import beblue.io.model.Artist;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class ArtistDao {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public List<Artist> findAll() {
//        Query query = em.createQuery("SELECT A FROM Artist A ORDER BY A.name ASC");
//        return query.getResultList();
//    }
//
//    public Artist findBySpotifyId(String spotify_id) {
//        if (spotify_id == null || spotify_id.isEmpty()) {
//            return null;
//        }
//        Query query = em.createQuery("SELECT A FROM Artist A WHERE A.spotify_id = :spotify_id");
//        query.setParameter("spotify_id", spotify_id);
//        if (!query.getResultList().isEmpty()) {
//            return (Artist) query.getSingleResult();
//        }
//        return null;
//    }
//
//    public Artist store(String name, String spotify_id) {
//        if (name == null || spotify_id == null || name.isEmpty() || spotify_id.isEmpty()) {
//            return null;
//        }
//        Artist artist = findBySpotifyId(spotify_id);
//        if (artist == null) {
//            em.getTransaction().begin();
//            try {
//                artist = new Artist();
//                artist.setName(name);
//                artist.setSpotify_id(spotify_id);
//                em.persist(artist);
//                em.flush();
//                em.getTransaction().commit();
//            } catch (Exception e) {
//                artist = null;
//                em.getTransaction().rollback();
//
//            }
//        }
//        return artist;
//
//    }
//
//}
