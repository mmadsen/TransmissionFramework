/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.mmadsen.sim.tf.agent.UnstructuredTraitAgentProvider;
import org.mmadsen.sim.tf.interfaces.*;
import org.mmadsen.sim.tf.models.BasicSimulationModel;
import org.mmadsen.sim.tf.structure.SimpleAgentTagProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 1:21:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicModelModule implements Module {
    public void configure(Binder binder) {
        binder.bind(ISimulationModel.class)
                .to(BasicSimulationModel.class)
                .in(Singleton.class);
        binder.bind(IAgent.class)
                .toProvider(UnstructuredTraitAgentProvider.class);
        binder.bind(ITraitDimension.class)
                .toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(ITrait.class)
                .toProvider(UnstructuredTraitProvider.class);
        binder.bind(IAgentTag.class)
                .toProvider(SimpleAgentTagProvider.class);
    }
}
