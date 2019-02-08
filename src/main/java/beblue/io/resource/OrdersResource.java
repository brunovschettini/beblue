package beblue.io.resource;

import beblue.io.helper.OrderResult;
import beblue.io.model.Albums;
import beblue.io.model.Orders;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import beblue.io.model.OrdersItems;
import beblue.io.model.Weeks;
import beblue.io.model.WeeksSales;
import beblue.io.repository.WeeksRepository;
import beblue.io.utils.Result;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import beblue.io.repository.OrdersItemsRepository;
import beblue.io.repository.UsersRepository;
import com.google.gson.Gson;

@RestController
public class OrdersResource {

    @Autowired
    private OrdersItemsRepository oir;

    @Autowired
    private WeeksRepository wr;

    @Autowired
    private UsersRepository ur;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/order/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find_id(@PathVariable("id") Long id) {
        Result result = new Result();
        if (id == null) {
            result.setStatus_code(0);
            result.setStatus("empty order id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        List<OrdersItems> ois = oir.findByOrder(id);
        if (ois == null || ois.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("order not found!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(ois, HttpStatus.OK);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    public ResponseEntity<?> store(@RequestBody List<Albums> albums) {
        // public ResponseEntity<?> store() {
        Result result = new Result();
        // List<Album> albums = null;
        if (albums == null || albums.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty order id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        List<OrdersItems> ois = new ArrayList<>();
        Orders orders = new Orders();
        Weeks weeks = wr.findByNumber_day(new Date().getDay());
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        orders.setUsers(ur.getOne(1L));
        em.getTransaction().begin();
        try {
            em.persist(orders);
            em.flush();
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        BigDecimal total = new BigDecimal(0);
        BigDecimal total_cashback = new BigDecimal(0);
        total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        for (Albums album : albums) {
            Query query = em.createQuery("SELECT WS FROM WeeksSales WS WHERE WS.genres.id = :genre_id AND WS.weeks.id = :weeks_id");
            query.setParameter("genre_id", album.getGenre().getId());
            query.setParameter("weeks_id", weeks.getId());
            WeeksSales ws = (WeeksSales) query.getSingleResult();
            OrdersItems oi = new OrdersItems();
            oi.setAlbums(album);
            oi.setOrders(orders);
            oi.setCost(album.getPrice());
            oi.setCashback_percent_log(ws.getPercent());
            total_cashback = total_cashback.add(oi.getCashback());
            total = total.add(oi.getCost());
            try {
                em.persist(oi);
                em.flush();
            } catch (Exception e) {
                result.setStatus_code(0);
                result.setStatus("e->" + e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            ois.add(oi);
        }
        em.getTransaction().commit();
        result.setStatus("success: order nº " + ois.get(0).getOrders().getId() + " registered");
        OrderResult or = new OrderResult(total, total_cashback, ois); 
        result.setResult(new Gson().toJson(or));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestBody Orders orders) {
        Result result = new Result();
        if (orders == null) {
            result.setStatus_code(0);
            result.setStatus("empty order!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("SELECT OI FROM OrdersItems OI WHERE OI.orders.id = :order_id");
        query.setParameter("order_id", orders.getId());
        List<OrdersItems> ois = query.getResultList();
        for (OrdersItems ordersItems : ois) {
            try {
                em.remove(ordersItems);
                em.flush();
            } catch (Exception e) {
                result.setStatus_code(0);
                result.setStatus("order items e->" + e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        try {
            em.remove(orders);
            em.flush();
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("order e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        em.getTransaction().commit();
        result.setStatus("success: order nº " + orders.getId() + " removed");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/item/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete_item(@RequestBody OrdersItems ordersItems) {
        Result result = new Result();
        if (ordersItems == null) {
            result.setStatus_code(0);
            result.setStatus("empty order!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        try {
            em.remove(ordersItems);
            em.flush();
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("order items e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        em.getTransaction().commit();
        result.setStatus("success: item nº " + ordersItems.getId() + " removed");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
