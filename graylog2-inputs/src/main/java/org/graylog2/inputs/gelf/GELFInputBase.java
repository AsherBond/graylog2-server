/*
 * Copyright 2012-2014 TORCH GmbH
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.graylog2.inputs.gelf;

import org.graylog2.plugin.buffers.Buffer;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationException;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.inputs.MessageInput;
import org.graylog2.plugin.inputs.MisfireException;
import org.graylog2.plugin.inputs.util.ConnectionCounter;
import org.graylog2.plugin.inputs.util.ThroughputCounter;
import org.jboss.netty.bootstrap.Bootstrap;
import org.jboss.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
public abstract class GELFInputBase extends MessageInput {

    public static final String CK_BIND_ADDRESS = "bind_address";
    public static final String CK_PORT = "port";

    protected Bootstrap bootstrap;
    protected Channel channel;

    protected final ThroughputCounter throughputCounter;
    protected final ConnectionCounter connectionCounter;

    protected InetSocketAddress socketAddress;

    public GELFInputBase() {
        this.throughputCounter = new ThroughputCounter();
        this.connectionCounter = new ConnectionCounter();
    }

    @Override
    public void checkConfiguration() throws ConfigurationException {
        if (!checkConfig(configuration)) {
            throw new ConfigurationException(configuration.getSource().toString());
        }

        this.socketAddress = new InetSocketAddress(
                configuration.getString(CK_BIND_ADDRESS),
                (int) configuration.getInt(CK_PORT)
        );
    }

    protected boolean checkConfig(Configuration config) {
        return config.stringIsSet(CK_BIND_ADDRESS)
                && config.intIsSet(CK_PORT);
    }

    @Override
    public void stop() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (bootstrap != null) {
            bootstrap.releaseExternalResources();
            bootstrap.shutdown();
        }
    }

    public ConfigurationRequest getRequestedConfiguration() {
        ConfigurationRequest r = new ConfigurationRequest();

        r.addField(ConfigurationRequest.Templates.bindAddress(CK_BIND_ADDRESS));
        r.addField(ConfigurationRequest.Templates.portNumber(CK_PORT, 12201));
        r.addField(ConfigurationRequest.Templates.recvBufferSize(CK_RECV_BUFFER_SIZE, 1024 * 1024));
        return r;
    }
    @Override
    public boolean isExclusive() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return configuration.getSource();
    }

    @Override
    public String getName() {
        throw new RuntimeException("Must be overridden in GELF input classes.");
    }

    @Override
    public void launch(Buffer processBuffer) throws MisfireException {
        throw new RuntimeException("Must be overridden in GELF input classes.");
    }

    @Override
    public String linkToDocs() {
        return "";
    }

}
