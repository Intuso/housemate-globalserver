package com.intuso.housemate.globalserver.ioc;

import com.google.common.collect.Lists;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

/**
 * Created by tomc on 24/01/17.
 */
public class ManagedCollectionFactoryImpl implements ManagedCollectionFactory {
    @Override
    public <LISTENER> ManagedCollection<LISTENER> create() {
        return new ManagedCollection<>(Lists.newCopyOnWriteArrayList());
    }
}
