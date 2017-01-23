package com.intuso.housemate.globalserver.api.globalserver.v1_0.ioc;

import com.intuso.housemate.globalserver.api.globalserver.v1_0.ClientResource;
import com.intuso.housemate.globalserver.api.globalserver.v1_0.UserResource;
import com.intuso.utilities.webserver.ioc.GuiceHK2BridgedResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServerV1_0ResourceConfig extends GuiceHK2BridgedResourceConfig {
    public GlobalServerV1_0ResourceConfig() {
        super(ClientResource.class,
                UserResource.class);
        register(JacksonFeature.class);
    }
}

