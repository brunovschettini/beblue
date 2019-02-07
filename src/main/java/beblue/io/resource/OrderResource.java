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
import beblue.io.repository.OrderItemsRepository;
import beblue.io.utils.Result;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.criterion.Order;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class OrderResource {

    @Autowired
    OrderItemsRepository oir;

    @PersistenceContext
    EntityManager em;

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

    @RequestMapping(value = "/order/add/{albums_ids}", method = RequestMethod.POST)
    public ResponseEntity<?> store(@RequestParam("albums_ids") String albums_ids) {
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
        em.getTransaction().begin();
        try {
            em.persist(orders);
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("e->" + e.getMessage());
            em.getTransaction().rollback();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        int weekday = new Date().getDay();
        for (Long album_id : listAlbumId) {
            Album album = (Album) em.find(Album.class, album_id);
            if (album == null) {
                result.setStatus_code(0);
                result.setStatus("album not found!");
                em.getTransaction().rollback();
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            OrdersItems oi = new OrdersItems();
            oi.setAlbum(album);
            oi.setOrders(orders);
            oi.setCashback_percent_log(BigDecimal.ZERO);
            oi.setTotal(album.getPrice());
            try {
                em.persist(oi);
            } catch (Exception e) {
                result.setStatus_code(0);
                result.setStatus("e->" + e.getMessage());
                em.getTransaction().rollback();
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        }
        em.flush();
        em.getTransaction().commit();
        result.setResult(ois);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
