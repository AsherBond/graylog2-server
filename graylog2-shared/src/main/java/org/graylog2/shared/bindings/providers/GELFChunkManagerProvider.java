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

package org.graylog2.shared.bindings.providers;

import com.codahale.metrics.MetricRegistry;
import org.graylog2.inputs.gelf.gelf.GELFChunkManager;
import org.graylog2.shared.buffers.ProcessBuffer;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public class GELFChunkManagerProvider implements Provider<GELFChunkManager> {
    private static GELFChunkManager gelfChunkManager = null;

    @Inject
    public GELFChunkManagerProvider(MetricRegistry metricRegistry,
                                    ProcessBuffer processBuffer) {
        if (gelfChunkManager == null)
            gelfChunkManager = new GELFChunkManager(metricRegistry, processBuffer);
    }

    @Override
    public GELFChunkManager get() {
        return gelfChunkManager;
    }
}
