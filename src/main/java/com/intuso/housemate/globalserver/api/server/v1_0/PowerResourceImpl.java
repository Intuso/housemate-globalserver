package com.intuso.housemate.globalserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanSerialiser;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyCommand;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyDevice;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyFeature;
import com.intuso.housemate.client.v1_0.rest.PowerResource;
import com.intuso.housemate.client.v1_0.rest.model.Page;
import com.intuso.housemate.globalserver.servers.Servers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 23/01/17.
 */
public class PowerResourceImpl implements PowerResource {

    private final static Logger logger = LoggerFactory.getLogger(PowerResourceImpl.class);

    private final Servers servers;

    private final Command.PerformListener<SimpleProxyCommand> loggerListener = new Command.PerformListener<SimpleProxyCommand>() {
        @Override
        public void commandStarted(SimpleProxyCommand command) {
            logger.debug("Started perform of {}", command);
        }

        @Override
        public void commandFinished(SimpleProxyCommand command) {
            logger.debug("Finished perform of {}", command);
        }

        @Override
        public void commandFailed(SimpleProxyCommand command, String error) {
            logger.debug("Failed perform of {} because {}", command, error);
        }
    };

    @Inject
    public PowerResourceImpl(Servers servers) {
        this.servers = servers;
    }

    @Override
    public Page<Device.Data> list(int offset, int limit) {
        List<Device.Data> devices = Lists.newArrayList();
        for(SimpleProxyDevice device : servers.getServer("294c7ff2-6dbd-4522-8f69-a94b6332cb73").getDevices()) {
            features:
            for (SimpleProxyFeature feature : device.getFeatures()) {
                if (feature.getCommands().get("on") != null) {
                    devices.add(new Device.Data(device.getId(), device.getName(), device.getDescription()));
                    break features;
                }
            }
        }
        Stream<Device.Data> stream  = devices.stream();
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, devices.size(), stream.collect(Collectors.toList()));
    }

    @Override
    public boolean isOn(String id) {
        SimpleProxyDevice device = servers.getServer("294c7ff2-6dbd-4522-8f69-a94b6332cb73").getDevices().get(id);
        for(SimpleProxyFeature feature : device.getFeatures())
            if(feature.getValues().get("on") != null)
                return BooleanSerialiser.INSTANCE.deserialise(feature.getValues().get("on").getValue().getElements().get(0));
        return false;
    }

    @Override
    public void turnOn(String id) {
        logger.debug("Turning on {}", id);
        SimpleProxyDevice device = servers.getServer("294c7ff2-6dbd-4522-8f69-a94b6332cb73").getDevices().get(id);
        for(SimpleProxyFeature feature : device.getFeatures()) {
            if (feature.getCommands().get("on") != null) {
                feature.getCommands().get("on").perform(loggerListener);
                return;
            }
        }
    }

    @Override
    public void turnOff(String id) {
        logger.debug("Turning of {}", id);
        SimpleProxyDevice device = servers.getServer("294c7ff2-6dbd-4522-8f69-a94b6332cb73").getDevices().get(id);
        for(SimpleProxyFeature feature : device.getFeatures()) {
            if (feature.getCommands().get("off") != null) {
                feature.getCommands().get("off").perform(loggerListener);
                return;
            }
        }
    }
}
