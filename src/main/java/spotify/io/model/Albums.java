package spotify.io.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "albums")
public class Albums implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String spotify_id;

    @Column(length = 255, nullable = false)
    private String name;

    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Artists artist;

    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Genres genre;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal price;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public Albums() {
        this.id = null;
        this.spotify_id = "";
        this.name = "";
        this.artist = null;
        this.genre = null;
        this.created_at = new Date();
    }

    public Albums(Long id, String spotify_id, String name, Artists artist, Genres genre, Date created_at) {
        this.id = id;
        this.spotify_id = spotify_id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artists getArtist() {
        return artist;
    }

    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
