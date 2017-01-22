package com.intuso.housemate.globalserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.intuso.housemate.globalserver.database.mongo.ioc.MongoDatabaseModule;
import com.intuso.housemate.globalserver.web.ioc.WebModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.addServlet(ServletHandler.Default404Servlet.class, "/");
        Injector injector = Guice.createInjector(new WebModule(), new MongoDatabaseModule());
        FilterHolder guiceFilter = new FilterHolder(injector.getInstance(GuiceFilter.class));
        handler.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));
        server.setHandler(handler);
        server.start();
    }
}
