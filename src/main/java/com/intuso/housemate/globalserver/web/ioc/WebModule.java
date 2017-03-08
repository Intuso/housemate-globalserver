package com.intuso.housemate.globalserver.web.ioc;

import com.google.inject.servlet.ServletModule;
import com.intuso.housemate.globalserver.web.api.ioc.ApiModule;
import com.intuso.housemate.globalserver.web.security.ioc.SecurityModule;
import com.intuso.housemate.globalserver.web.ui.ioc.UIModule;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;

/**
 * Created by tomc on 21/01/17.
 */
public class WebModule extends ServletModule {

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        UIModule.configureDefaults(defaultProperties);
    }

    @Override
    protected void configureServlets() {
        install(new ApiModule());
        install(new SecurityModule());
        install(new UIModule());
    }
}
