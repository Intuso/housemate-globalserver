package com.intuso.housemate.globalserver.web.jersey;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.ioc.GuiceHK2BridgedResourceConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Created by tomc on 21/01/17.
 */
public class JerseyResourceModule extends ServletModule {

    private final String resourceRoot;
    private final Class<? extends GuiceHK2BridgedResourceConfig> resourceConfigClass;

    public JerseyResourceModule(String resourceRoot, Class<? extends GuiceHK2BridgedResourceConfig> resourceConfigClass) {
        this.resourceRoot = resourceRoot.endsWith("/") ? resourceRoot : resourceRoot + "/";
        this.resourceConfigClass = resourceConfigClass;
    }

    @Override
    protected void configureServlets() {
        super.configureServlets();
        requestStaticInjection(GuiceHK2BridgedResourceConfig.class);
        ServletContainer servletContainer = new ServletContainer(ResourceConfig.forApplicationClass(resourceConfigClass));
        serve(resourceRoot + "*").with(servletContainer);
    }
}
