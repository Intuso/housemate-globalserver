package com.intuso.housemate.globalserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.rest.PowerResource;
import com.intuso.housemate.client.v1_0.rest.model.Page;

/**
 * Created by tomc on 23/01/17.
 */
public class PowerResourceImpl implements PowerResource {
    @Override
    public Page<Device.Data> list(int offset, int limit) {
        return new Page<>(0, 3, Lists.newArrayList(
                new Device.Data("lights", "Lights", "All the lights"),
                new Device.Data("lrlights", "Living Room Lights", "The lights in the living room"),
                new Device.Data("kfan", "Kitchen Fan", "Extractor fan in the kitchen")
        ));
    }

    @Override
    public boolean isOn(String id) {
        return true;
    }

    @Override
    public void turnOn(String id) {
        System.out.println("Turning on " + id);
    }

    @Override
    public void turnOff(String id) {
        System.out.println("Turning off " + id);
    }
}
