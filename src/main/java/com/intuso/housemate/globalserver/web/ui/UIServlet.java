package com.intuso.housemate.globalserver.web.ui;

import com.google.inject.Inject;
import com.intuso.housemate.globalserver.GlobalServer;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.eclipse.jetty.servlet.DefaultServlet;

import java.io.File;

/**
 * Created by tomc on 08/03/17.
 */
public class UIServlet extends DefaultServlet {

    public final static String UI_ROOT = "web.ui.directory";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(UI_ROOT, defaultProperties.get(GlobalServer.HOUSEMATE_CONFIG_DIR) + File.separator + "ui");
    }

    private final PropertyRepository properties;

    @Inject
    public UIServlet(PropertyRepository properties) {
        this.properties = properties;
    }

    @Override
    public String getInitParameter(String name) {
        if("resourceBase".equals(name))
            return properties.get(UI_ROOT);
        return super.getInitParameter(name);
    }
}
