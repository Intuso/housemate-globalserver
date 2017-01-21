package com.intuso.housemate.globalserver.web.oauth;

import com.intuso.housemate.globalserver.web.ioc.GuiceHK2BridgedResourceConfig;

/**
 * Created by tomc on 21/01/17.
 */
public class OAuthResourceConfig extends GuiceHK2BridgedResourceConfig {
    public OAuthResourceConfig() {
        super(OAuthResource.class);
    }
}
