package beblue.io.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column; 
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class WeeksSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Weeks weeks;

    @JoinColumn
    @ManyToOne
    private Genre genre;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(3,2) DEFAULT 0")
    private BigDecimal percent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public WeeksSale() {
        this.id = null;
        this.weeks = null;
        this.genre = null;
        this.percent = BigDecimal.ZERO;
        this.created_at = new Date();
    }

    public WeeksSale(Long id, Weeks weeks, Genre genre, BigDecimal percent, Date created_at) {
        this.id = id;
        this.weeks = weeks;
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

    public Weeks getWeeks() {
        return weeks;
    }

    public void setWeeks(Weeks weeks) {
        this.weeks = weeks;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
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
