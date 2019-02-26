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
@Table(name="weeks_sales")
public class WeeksSales implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name = "weeks_id", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Weeks week;

    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Genres genre;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal percent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public WeeksSales() {
        this.id = null;
        this.week = null;
        this.genre = null;
        this.percent = BigDecimal.ZERO;
        this.created_at = new Date();
    }

    public WeeksSales(Long id, Weeks week, Genres genre, BigDecimal percent, Date created_at) {
        this.id = id;
        this.week = week;
        this.genre = genre;
        this.percent = percent;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weeks getWeek() {
        return week;
    }

    public void setWeek(Weeks week) {
        this.week = week;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenres(Genres genre) {
        this.genre = genre;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
