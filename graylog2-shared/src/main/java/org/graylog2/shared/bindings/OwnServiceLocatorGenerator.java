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

package org.graylog2.shared.bindings;

import com.google.inject.Injector;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.extension.ServiceLocatorGenerator;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.jvnet.hk2.external.generator.ServiceLocatorGeneratorImpl;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.jvnet.hk2.internal.*;

import javax.inject.Singleton;

/**
 * @author Dennis Oelkers <dennis@torch.sh>
 */
public class OwnServiceLocatorGenerator extends ServiceLocatorGeneratorImpl implements ServiceLocatorGenerator {
    private final Injector injector;
    public OwnServiceLocatorGenerator(Injector injector) {
        super();
        this.injector = injector;
    }

    private ServiceLocator initialize(String name, ServiceLocator parent) {
        if (parent != null && !(parent instanceof ServiceLocatorImpl)) {
            throw new AssertionError("parent must be a " + ServiceLocatorImpl.class.getName() +
                    " instead it is a " + parent.getClass().getName());
        }

        //ServiceLocatorImpl sli = new OwnServiceLocator(name, (ServiceLocatorImpl) parent, injector);
        ServiceLocatorImpl sli = new ServiceLocatorImpl(name, (ServiceLocatorImpl) parent);

        DynamicConfigurationImpl dci = new DynamicConfigurationImpl(sli);

        // The service locator itself
        dci.bind(Utilities.getLocatorDescriptor(sli));

        // The injection resolver for three thirty
        dci.addActiveDescriptor(Utilities.getThreeThirtyDescriptor(sli));

        // The dynamic configuration utility
        dci.bind(BuilderHelper.link(DynamicConfigurationServiceImpl.class, false).
                to(DynamicConfigurationService.class).
                in(Singleton.class.getName()).
                localOnly().
                build());

        dci.bind(BuilderHelper.createConstantDescriptor(
                new DefaultClassAnalyzer(sli)));

        dci.commit();

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(sli);
        GuiceIntoHK2Bridge guiceBridge = sli.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(injector);

        return sli;
    }

    @Override
    public ServiceLocator create(String name, ServiceLocator parent) {
        return initialize(name, parent);
    }
}
