package br.com.beblue.ws;

import br.com.beblue.utils.NotifyResponse;
import com.google.gson.Gson;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/disks")
public class DisksWS {

    @Context
    HttpHeaders headers;

    @GET
    @Path("/genre/{genre}")
    @Produces({MediaType.APPLICATION_JSON})
    public synchronized Response importCities(@PathParam("genre") String genre) {
        Gson gson = new Gson();
        NotifyResponse notifyResponse = new NotifyResponse();
        notifyResponse.setObject("OK");
        return Response.status(200).entity(gson.toJson(notifyResponse)).build();
    }

}
