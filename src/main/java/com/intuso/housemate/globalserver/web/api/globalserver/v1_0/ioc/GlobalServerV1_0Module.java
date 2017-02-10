package com.intuso.housemate.globalserver.web.api.globalserver.v1_0.ioc;

import com.intuso.utilities.webserver.ioc.JerseyResourcesModule;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServerV1_0Module extends JerseyResourcesModule {
    public GlobalServerV1_0Module() {
        super("/api/globalserver/1.0/", GlobalServerV1_0ResourceConfig.class);
    }
}
