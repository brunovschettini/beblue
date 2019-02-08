package beblue.io.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"orders_id", "albums_id"}))
public class OrdersItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Orders orders;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Albums albums;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal cashback_percent_log;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal cost;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public OrdersItems() {
        this.id = null;
        this.orders = null;
        this.albums = null;
        this.cashback_percent_log = BigDecimal.ZERO;
        this.cost = BigDecimal.ZERO;
        this.cost.setScale(2, RoundingMode.HALF_EVEN);
        this.created_at = new Date();
    }

    public OrdersItems(Long id, Orders orders, Albums albums, BigDecimal cashback_percent_log, BigDecimal cost, Date created_at) {
        this.id = id;
        this.orders = orders;
        this.albums = albums;
        this.cashback_percent_log = cashback_percent_log;
        this.cost = cost;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Albums getAlbums() {
        return albums;
    }

    public void setAlbums(Albums albums) {
        this.albums = albums;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCashback_percent_log() {
        return cashback_percent_log;
    }

    public void setCashback_percent_log(BigDecimal cashback_percent_log) {
        this.cashback_percent_log = cashback_percent_log;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public BigDecimal getCashback() {
        double cashback_calc = ((cost.doubleValue() * cashback_percent_log.doubleValue()) / 100);
        BigDecimal bd = new BigDecimal(cashback_calc);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return bd;
    }

}
