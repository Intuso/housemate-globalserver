package com.intuso.housemate.globalserver.web.api.v1_0;

import com.google.common.collect.Maps;

import javax.ws.rs.*;
import java.util.Map;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/devices")
public class DevicesResource {

    @GET
    @Produces("application/json")
    public Map<String, String> listNames() {
        Map<String, String> result = Maps.newHashMap();
        result.put("lights", "Lights");
        result.put("lrlights", "Living Room Lights");
        result.put("kfan", "Kitchen Fan");
        return result;
    }

    @POST
    @Path("/{id}/on")
    public void turnOn(@PathParam("id") String id) {
        System.out.println("Turned on: " + id);
    }

    @POST
    @Path("/{id}/off")
    public void turnOff(@PathParam("id") String id) {
        System.out.println("Turned on: " + id);
    }
}
