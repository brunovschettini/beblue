package br.com.beblue.api;

import br.com.beblue.conn.Conn;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusApi {

    @GET
    @Produces({MediaType.TEXT_HTML})
    public synchronized Response active() {
        

        Conn conn = new Conn();
        conn.getEntityManager();
        
        return Response.status(200).entity("1").build();
    }
}
