package spotify.io.helper;

import spotify.io.model.OrdersItems;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderResult implements Serializable {

    private BigDecimal total;
    private BigDecimal total_cashback;
    private List<OrdersItems> ordersItems;

    public OrderResult(BigDecimal total, BigDecimal total_cashback, List<OrdersItems> ordersItems) {
        this.total = total;
        this.total_cashback = total_cashback;
        this.ordersItems = ordersItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getTotal_cashback() {
        return total_cashback;
    }

    public void setTotal_cashback(BigDecimal total_cashback) {
        this.total_cashback = total_cashback;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrdersItems> getOrdersItems() {
        return ordersItems;
    }

    public void setOrdersItems(List<OrdersItems> ordersItems) {
        this.ordersItems = ordersItems;
    }
}
