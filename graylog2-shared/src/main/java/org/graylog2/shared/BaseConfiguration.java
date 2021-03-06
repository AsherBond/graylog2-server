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

package org.graylog2.shared;

import com.github.joschi.jadconfig.Parameter;
import com.github.joschi.jadconfig.validators.PositiveIntegerValidator;
import com.lmax.disruptor.*;
import org.graylog2.plugin.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public abstract class BaseConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(BaseConfiguration.class);

    @Parameter(value = "rest_transport_uri", required = false)
    private String restTransportUri;

    @Parameter(value = "processbuffer_processors", required = true, validator = PositiveIntegerValidator.class)
    private int processBufferProcessors = 5;

    @Parameter(value = "processor_wait_strategy", required = true)
    private String processorWaitStrategy = "blocking";

    @Parameter(value = "rest_enable_cors", required = false)
    private boolean restEnableCors = false;

    @Parameter(value = "rest_enable_gzip", required = false)
    private boolean restEnableGzip = false;

    @Parameter(value = "groovy_shell_enable", required = false)
    private boolean groovyShellEnable = false;

    @Parameter(value = "groovy_shell_port", required = false)
    private int groovyShellPort = 6789;

    @Parameter(value = "plugin_dir", required = false)
    private String pluginDir = "plugin";

    public URI getRestTransportUri() {
        if (restTransportUri == null || restTransportUri.isEmpty()) {
            return null;
        }

        return Tools.getUriStandard(restTransportUri);
    }

    public void setRestTransportUri(String restTransportUri) {
        this.restTransportUri = restTransportUri;
    }

    public int getProcessBufferProcessors() {
        return processBufferProcessors;
    }

    public WaitStrategy getProcessorWaitStrategy() {
        if (processorWaitStrategy.equals("sleeping")) {
            return new SleepingWaitStrategy();
        }

        if (processorWaitStrategy.equals("yielding")) {
            return new YieldingWaitStrategy();
        }

        if (processorWaitStrategy.equals("blocking")) {
            return new BlockingWaitStrategy();
        }

        if (processorWaitStrategy.equals("busy_spinning")) {
            return new BusySpinWaitStrategy();
        }

        LOG.warn("Invalid setting for [processor_wait_strategy]:"
                + " Falling back to default: BlockingWaitStrategy.");
        return new BlockingWaitStrategy();
    }

    public boolean isRestEnableCors() {
        return restEnableCors;
    }

    public boolean isRestEnableGzip() {
        return restEnableGzip;
    }

    public boolean isGroovyShellEnable() {
        return groovyShellEnable;
    }

    public int getGroovyShellPort() {
        return groovyShellPort;
    }

    public String getPluginDir() {
        return pluginDir;
    }

    public abstract String getNodeIdFile();
    public abstract URI getRestListenUri();
}
