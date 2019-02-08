//package beblue.io.model;
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//@Entity
//public class Cashbacks implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(length = 255, nullable = false, unique = true)
//    private String spotify_id;
//
//    @Column(length = 255, nullable = false)
//    private String name;
//
//    @JoinColumn
//    @ManyToOne
//    private Artists artists;
//
//    @JoinColumn
//    @ManyToOne
//    private Genres genres;
//
//    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
//    private BigDecimal price;
//
//    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date created_at;
//
//    public Cashbacks() {
//        this.id = null;
//        this.spotify_id = "";
//        this.name = "";
//        this.artists = null;
//        this.genres = null;
//        this.created_at = new Date();
//    }
//
//    public Cashbacks(Long id, String spotify_id, String name, Artists artists, Genres genres, Date created_at) {
//        this.id = id;
//        this.spotify_id = spotify_id;
//        this.name = name;
//        this.artists = artists;
//        this.genres = genres;
//        this.created_at = created_at;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getSpotify_id() {
//        return spotify_id;
//    }
//
//    public void setSpotify_id(String spotify_id) {
//        this.spotify_id = spotify_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Artists getArtists() {
//        return artists;
//    }
//
//    public void setArtist(Artists artists) {
//        this.artists = artists;
//    }
//
//    public Genres getGenres() {
//        return genres;
//    }
//
//    public void setGenres(Genres genres) {
//        this.genres = genres;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//
//    public Date getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(Date created_at) {
//        this.created_at = created_at;
//    }
//
//}
