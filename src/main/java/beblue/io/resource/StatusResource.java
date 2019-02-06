package beblue.io.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusResource {

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public synchronized ResponseEntity<Object> active() {
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
