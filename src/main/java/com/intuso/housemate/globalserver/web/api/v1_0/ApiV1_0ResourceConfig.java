package com.intuso.housemate.globalserver.web.api.v1_0;

import com.intuso.housemate.globalserver.web.ioc.GuiceHK2BridgedResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * Created by tomc on 21/01/17.
 */
public class ApiV1_0ResourceConfig extends GuiceHK2BridgedResourceConfig {
    public ApiV1_0ResourceConfig() {
        super(ApiV1_0Resource.class);
        register(JacksonFeature.class);
    }
}
