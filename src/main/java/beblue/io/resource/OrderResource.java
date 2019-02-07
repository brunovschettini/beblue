package beblue.io.resource;

import beblue.io.model.Album;
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
import beblue.io.model.WeeksSales;
import beblue.io.repository.OrderItemsRepository;
import beblue.io.utils.Result;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@RestController
public class OrderResource {

    @Autowired
    private OrderItemsRepository oir;

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

    @RequestMapping(value = "/order/add/{albums_ids}", method = RequestMethod.GET)
    public ResponseEntity<?> store(@PathVariable("albums_ids") String albums_ids) {
        Result result = new Result();
        if (albums_ids == null || albums_ids.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty order id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        List<Long> listAlbumId = new ArrayList<>();
        if (albums_ids.contains(",")) {
            for (String a : albums_ids.split(",")) {
                try {
                    listAlbumId.add(Long.parseLong(a));
                } catch (NumberFormatException e) {
                    result.setStatus_code(0);
                    result.setStatus("invalid number format!");
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }
        } else {
            listAlbumId.add(Long.parseLong(albums_ids));
        }
        List<OrdersItems> ois = new ArrayList<>();
        Orders orders = new Orders();
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(orders);
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        int weekday = new Date().getDay();
        for (Long album_id : listAlbumId) {
            Album album = (Album) entityManager.find(Album.class, album_id);
            if (album == null) {
                result.setStatus_code(0);
                result.setStatus("album not found!");
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            Query query = entityManager.createQuery("SELECT WS FROM WeeksSales WS WHERE WS.genre.id = :genre_id AND WS.weeks.number_day = :number_day");
            entityManager.setProperty("genre_id", album.getGenre().getId());
            entityManager.setProperty("number_day", weekday);
            WeeksSales ws = (WeeksSales) query.getSingleResult();
            OrdersItems oi = new OrdersItems();
            oi.setAlbum(album);
            oi.setOrders(orders);
            oi.setCashback_percent_log(ws.getPercent());
            double total = album.getPrice().doubleValue() - ((album.getPrice().doubleValue() * ws.getPercent().doubleValue()) / 100);
            oi.setOriginal_price(album.getPrice());
            oi.setTotal(new BigDecimal(total));
            try {
                entityManager.persist(oi);
            } catch (Exception e) {
                result.setStatus_code(0);
                result.setStatus("e->" + e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        result.setResult(ois);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
