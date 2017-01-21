package com.intuso.housemate.globalserver.web.api.v1_0;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/test")
public class TestOAuthResource {

    @GET
    public String test() {
        return "Success!!";
    }
}
