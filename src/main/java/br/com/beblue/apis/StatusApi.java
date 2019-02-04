package br.com.beblue.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusWS {

    @GET
    @Path("/active")
    @Produces({MediaType.TEXT_HTML})
    public synchronized Response active() {
        return Response.status(200).entity("1").build();
    }
}
