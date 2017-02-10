package com.intuso.housemate.globalserver;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.globalserver.database.mongo.ioc.MongoDatabaseModule;
import com.intuso.housemate.globalserver.ioc.GlobalServerModule;

import java.io.File;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServer {

    public static void main(String[] args) throws Exception {
        Logback.configure(new File("."));
        Injector injector = Guice.createInjector(new GlobalServerModule(),
                new MongoDatabaseModule());
        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        serviceManager.startAsync();
        serviceManager.awaitHealthy();
    }
}
