package com.intuso.housemate.globalserver;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intuso.housemate.globalserver.ioc.GlobalServerModule;
import com.intuso.housemate.webserver.database.mongo.ioc.MongoDatabaseModule;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.properties.reader.commandline.CommandLinePropertyRepository;
import com.intuso.utilities.properties.reader.file.FilePropertyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tomc on 21/01/17.
 */
public class GlobalServer {

    public final static String HOUSEMATE_CONFIG_DIR = "conf.dir";
    public final static String HOUSEMATE_PROPS_FILE = "conf.file";

    public static void main(String[] args) throws Exception {

        final ManagedCollectionFactory managedCollectionFactory = new ManagedCollectionFactory() {
            @Override
            public <LISTENER> ManagedCollection<LISTENER> create() {
                return new ManagedCollection<>(new CopyOnWriteArrayList<LISTENER>());
            }
        };
        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(managedCollectionFactory);

        defaultProperties.set(HOUSEMATE_CONFIG_DIR, System.getProperty("user.home") + File.separator + ".housemate");
        defaultProperties.set(HOUSEMATE_PROPS_FILE, "housemate.props");

        // read the command lines args now so we can use them to setup the file properties
        CommandLinePropertyRepository clProperties = new CommandLinePropertyRepository(managedCollectionFactory, defaultProperties, args);

        // read system file properties
        File configDirectory = new File(clProperties.get(HOUSEMATE_CONFIG_DIR));
        // create the directory if it does not exist
        if(!configDirectory.exists())
            configDirectory.mkdirs();

        Logback.configure(configDirectory);

        Logger logger = LoggerFactory.getLogger(GlobalServer.class);

        // get the props file
        File props_file = new File(configDirectory, clProperties.get(HOUSEMATE_PROPS_FILE));
        FilePropertyRepository propsFile = null;
        try {
            if(!props_file.exists())
                props_file.createNewFile();
            propsFile = new FilePropertyRepository(managedCollectionFactory, defaultProperties, props_file);
        } catch (FileNotFoundException e) {
            logger.warn("Could not find system properties file \"" + props_file.getAbsolutePath() + "\"");
        } catch (IOException e) {
            logger.error("Could not read system properties from file");
            e.printStackTrace();
        }

        // wrap the defaults and file properties with the command line properties
        PropertyRepository properties = new CommandLinePropertyRepository(managedCollectionFactory,
                        propsFile != null ? propsFile : defaultProperties, args);

        // configure the defaults
        GlobalServerModule.configureDefaults(defaultProperties);
        MongoDatabaseModule.configureDefaults(defaultProperties);

        // create the app injector
        Injector injector = Guice.createInjector(new GlobalServerModule(properties),
                new MongoDatabaseModule());

        // start the app by starting the service manager
        ServiceManager serviceManager = injector.getInstance(ServiceManager.class);
        serviceManager.startAsync();
        serviceManager.awaitHealthy();
    }
}
