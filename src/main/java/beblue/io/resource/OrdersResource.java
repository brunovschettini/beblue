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
import beblue.io.repository.AlbumsRepository;
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
import beblue.io.repository.OrdersRepository;
import beblue.io.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class OrdersResource {

    @Autowired
    private OrdersItemsRepository oir;

    @Autowired
    private OrdersRepository oi;

    @Autowired
    private WeeksRepository wr;

    @Autowired
    private UsersRepository ur;

    @Autowired
    private AlbumsRepository a;

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
        BigDecimal total = new BigDecimal(0);
        BigDecimal total_cashback = new BigDecimal(0);
        for (OrdersItems oi : ois) {
            total_cashback = total_cashback.add(oi.getCashback());
            total = total.add(oi.getCost());
        }
        OrderResult or = new OrderResult(total, total_cashback, ois);

        if (ois == null || ois.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("order not found!");
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        result.setStatus("info: order nº " + ois.get(0).getOrder().getId());
        result.setResult(or);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String someJsonString = null;
        try {
            someJsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Result r = mapper.readValue(someJsonString, Result.class);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/order/find/", method = RequestMethod.GET)
    public ResponseEntity<?> find_query() {
        return find_query("all");
    }

    @RequestMapping(value = "/order/find/{q}", method = RequestMethod.GET)
    public ResponseEntity<?> find_query(@PathVariable("q") String q) {
        Result result = new Result();
        if (q == null || q.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty query!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        List<OrdersItems> ois = null;

        if (q.equals("all")) {
            ois = oir.findByAll();
        } else {
            JSONObject g = new JSONObject(q);
            if (g.getString("start_date") != null && g.getString("end_date") != null && !g.getString("start_date").isEmpty() && !g.getString("end_date").isEmpty()) {
                Date sd = null;
                Date ed = null;
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    sd = formato.parse(g.getString("start_date").replace("-", "/"));
                    ed = formato.parse(g.getString("end_date").replace("-", "/"));
                    ois = oir.findByDates(sd, ed);
                } catch (ParseException ex) {
                    Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ois = oir.findByAll();
            }
        }
        if (ois == null) {
            result.setStatus_code(0);
            result.setStatus("order not found!");
            return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        }
        BigDecimal total = new BigDecimal(0);
        BigDecimal total_cashback = new BigDecimal(0);
        for (OrdersItems oi : ois) {
            total_cashback = total_cashback.add(oi.getCashback());
            total = total.add(oi.getCost());
        }
        OrderResult or = new OrderResult(total, total_cashback, ois);
        result.setStatus("info: list orders by range date");
        result.setResult(or);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String someJsonString = null;
        try {
            someJsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Result r = mapper.readValue(someJsonString, Result.class);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        orders.setUser(ur.getOne(1L));
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
            Query query = em.createQuery("SELECT WS FROM WeeksSales WS WHERE WS.genre.id = :genre_id AND WS.week.id = :week_id");
            query.setParameter("genre_id", album.getGenre().getId());
            query.setParameter("week_id", weeks.getId());
            WeeksSales ws = (WeeksSales) query.getSingleResult();
            OrdersItems oi = new OrdersItems();
            oi.setAlbum(album);
            oi.setOrder(orders);
            oi.setCost(album.getPrice());
            oi.setCashback_percent_log(ws.getPercent());
            oi.setCashback(oi.calcCashback());
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
        result.setStatus("success: order nº " + ois.get(0).getOrder().getId() + " registered");
        OrderResult or = new OrderResult(total, total_cashback, ois);
        result.setResult(or);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String someJsonString = null;
        try {
            someJsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Result r = mapper.readValue(someJsonString, Result.class);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/add2", method = RequestMethod.POST)
    public ResponseEntity<?> store_id(@RequestParam("albums_id") String albums_id) {
        // public ResponseEntity<?> store() {
        Result result = new Result();
        // List<Album> albums = null;
        if (albums_id == null || albums_id.isEmpty()) {
            result.setStatus_code(0);
            result.setStatus("empty order id!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        String replace = albums_id.replace("[", "");
        System.out.println(replace);
        String replace1 = replace.replace("]", "");
        System.out.println(replace1);
        List<Long> listAlbumsId = Arrays
                .stream(replace1.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Albums> albums = a.findAllById(listAlbumsId);
        List<OrdersItems> ois = new ArrayList<>();
        Orders orders = new Orders();
        Weeks weeks = wr.findByNumber_day(new Date().getDay());
        EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        orders.setUser(ur.getOne(1L));
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
            Query query = em.createQuery("SELECT WS FROM WeeksSales WS WHERE WS.genre.id = :genre_id AND WS.week.id = :week_id");
            query.setParameter("genre_id", album.getGenre().getId());
            query.setParameter("week_id", weeks.getId());
            WeeksSales ws = (WeeksSales) query.getSingleResult();
            OrdersItems oi = new OrdersItems();
            oi.setAlbum(album);
            oi.setOrder(orders);
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
        result.setStatus("success: order nº " + ois.get(0).getOrder().getId() + " registered");
        OrderResult or = new OrderResult(total, total_cashback, ois);
        result.setResult(or);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String someJsonString = null;
        try {
            someJsonString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Result r = mapper.readValue(someJsonString, Result.class);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (IOException ex) {
            Logger.getLogger(OrdersResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Result result = new Result();
        if (id == null) {
            result.setStatus_code(0);
            result.setStatus("empty order!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        Orders orders = oi.getOne(id);
        List<OrdersItems> ois = oir.findByOrder(id);
        // EntityManager em = entityManager.getEntityManagerFactory().createEntityManager();
        // em.getTransaction().begin();
        for (OrdersItems ordersItems : ois) {
            try {
                oir.delete(ordersItems);
                // em.flush();
            } catch (Exception e) {
                result.setStatus_code(0);
                result.setStatus("order items e->" + e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        try {
            oi.delete(orders);
            // em.flush();
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("order e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.setStatus("success: order nº " + orders.getId() + " removed");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @RequestMapping(value = "/order/item/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete_item(@PathVariable("id") Long id) {
        OrdersItems ordersItems = oir.getOne(id);
        Result result = new Result();
        if (ordersItems == null) {
            result.setStatus_code(0);
            result.setStatus("empty order!");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        try {
            oir.delete(ordersItems);
        } catch (Exception e) {
            result.setStatus_code(0);
            result.setStatus("order items e->" + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.setStatus("success: item nº " + ordersItems.getId() + " removed");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
