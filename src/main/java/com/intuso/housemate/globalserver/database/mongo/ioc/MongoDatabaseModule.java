package com.intuso.housemate.globalserver.database.mongo.ioc;

import com.google.inject.AbstractModule;
import com.intuso.housemate.globalserver.database.Database;
import com.intuso.housemate.globalserver.database.mongo.MongoDatabaseImpl;

/**
 * Created by tomc on 21/01/17.
 */
public class MongoDatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Database.class).to(MongoDatabaseImpl.class);
    }
}
