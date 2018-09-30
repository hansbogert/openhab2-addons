/**
 * Copyright (c) 2014,2018 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.omnikinverter.handler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.measure.quantity.Energy;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.unit.SmartHomeUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.omnikinverter.OmnikInverter;
import org.openhab.binding.omnikinverter.OmnikInverterBindingConstants;
import org.openhab.binding.omnikinverter.OmnikInverterConfiguration;
import org.openhab.binding.omnikinverter.OmnikInverterMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OmnikInverterHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Hans van den Bogert - Initial contribution
 */
@NonNullByDefault
public class OmnikInverterHandler extends BaseThingHandler {
    @Nullable
    private OmnikInverter inverter;

    @Nullable
    private OmnikInverterMessage message;

    @Nullable
    private ScheduledFuture<?> pollingJob;

    private final Logger logger = LoggerFactory.getLogger(OmnikInverterHandler.class);

    @Nullable
    private OmnikInverterConfiguration config;

    public OmnikInverterHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(OmnikInverterBindingConstants.CHANNEL_POWER)) {
            if (command instanceof RefreshType) {
                updateData();
            }
        }
    }

    private void updateData() {
        try {
            message = inverter.pullCurrentStats();
            updateStatus(ThingStatus.ONLINE);

            QuantityType powerQuantity = new QuantityType<>(message.getPower(), SmartHomeUnits.WATT);
            updateState(OmnikInverterBindingConstants.CHANNEL_POWER, powerQuantity);

            updateState(OmnikInverterBindingConstants.CHANNEL_ENERGY_TODAY,
                    new QuantityType<Energy>(message.getEnergyToday(), SmartHomeUnits.KILOWATT_HOUR));

        } catch (UnknownHostException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Unknown host provided");
        } catch (java.net.NoRouteToHostException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "No route to host");
        } catch (ConnectException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Host does not allow socket connection");
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, "Unknown communication exception");
        } catch (Exception e) {
            e.printStackTrace();
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.NONE, "Unknown exception");
        }

    }

    @Override
    public void initialize() {
        config = getConfigAs(OmnikInverterConfiguration.class);
        inverter = new OmnikInverter(config.omnikHostname, config.omnikPort, config.omnikSerial);

        pollingJob = scheduler.scheduleWithFixedDelay(this::updateData, 0, 10, TimeUnit.SECONDS);
    }
}
