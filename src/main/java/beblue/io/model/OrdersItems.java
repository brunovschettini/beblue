package beblue.io.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class OrdersItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne
    private Orders orders;

    @JoinColumn
    @ManyToOne
    private Album album;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal cashback_percent_log;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal original_price;

    @Column(nullable = false, precision = 20, scale = 2, columnDefinition = "DECIMAL(20,2) DEFAULT 0")
    private BigDecimal total;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public OrdersItems() {
        this.id = null;
        this.orders = null;
        this.album = null;
        this.original_price = BigDecimal.ZERO;
        this.cashback_percent_log = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.created_at = new Date();
    }

    public OrdersItems(Long id, Orders orders, Album album, BigDecimal original_price, BigDecimal cashback_percent_log, BigDecimal total, Date created_at) {
        this.id = id;
        this.orders = orders;
        this.album = album;
        this.original_price = original_price;
        this.cashback_percent_log = cashback_percent_log;
        this.total = total;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public BigDecimal getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(BigDecimal original_price) {
        this.original_price = original_price;
    }

}
