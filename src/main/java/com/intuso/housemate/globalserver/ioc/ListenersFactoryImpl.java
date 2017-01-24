package com.intuso.housemate.globalserver.ioc;

import com.google.common.collect.Lists;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;

/**
 * Created by tomc on 24/01/17.
 */
public class ListenersFactoryImpl implements ListenersFactory {
    @Override
    public <LISTENER> Listeners<LISTENER> create() {
        return new Listeners<>(Lists.newCopyOnWriteArrayList());
    }
}
