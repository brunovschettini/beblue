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
@Table(name = "orders_items", uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "album_id"}))
public class OrdersItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Orders order;

    @JoinColumn(name = "album_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Albums album;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal cashback_percent_log;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal cost;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created_at;

    public OrdersItems() {
        this.id = null;
        this.order = null;
        this.album = null;
        this.cashback_percent_log = BigDecimal.ZERO;
        this.cost = BigDecimal.ZERO;
        this.cost.setScale(2, RoundingMode.HALF_EVEN);
        this.created_at = new Date();
    }

    public OrdersItems(Long id, Orders order, Albums album, BigDecimal cashback_percent_log, BigDecimal cost, Date created_at) {
        this.id = id;
        this.order = order;
        this.album = album;
        this.cashback_percent_log = cashback_percent_log;
        this.cost = cost;
        this.created_at = created_at;
    }

    public BigDecimal getCashback() {
        double cashback_calc = ((cost.doubleValue() * cashback_percent_log.doubleValue()) / 100);
        BigDecimal bd = new BigDecimal(cashback_calc);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return bd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Albums getAlbum() {
        return album;
    }

    public void setAlbum(Albums album) {
        this.album = album;
    }

    public BigDecimal getCashback_percent_log() {
        return cashback_percent_log;
    }

    public void setCashback_percent_log(BigDecimal cashback_percent_log) {
        this.cashback_percent_log = cashback_percent_log;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
