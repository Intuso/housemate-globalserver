package com.intuso.housemate.globalserver;

import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by tomc on 24/01/17.
 */
public class ManagedCollectionFactoryImpl implements ManagedCollectionFactory {

    @Override
    public <LISTENER> ManagedCollection<LISTENER> createSet() {
        return new ManagedCollection<>(Collections.synchronizedSet(new HashSet<>()));
    }

    @Override
    public <LISTENER> ManagedCollection<LISTENER> createList() {
        return new ManagedCollection<>(Collections.synchronizedList(new LinkedList<>()));
    }
}
