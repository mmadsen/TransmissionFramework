/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.util;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.madsenlab.sim.tf.agent.UnstructuredMultidimensionalTraitAgentProvider;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.models.ConfigurableSimulationModel;
import org.madsenlab.sim.tf.population.SimpleAgentDemeProvider;
import org.madsenlab.sim.tf.population.SimpleAgentPopulationProvider;
import org.madsenlab.sim.tf.structure.SimpleAgentTagProvider;
import org.madsenlab.sim.tf.structure.WellMixedInteractionTopologyProvider;
import org.madsenlab.sim.tf.traits.RealTraitProvider;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.madsenlab.sim.tf.utils.LogFileHandler;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 4/13/11
 * Time: 11:25 AM
 */

public abstract class MultidimensionalAgentModule implements Module {

    public void configure(Binder binder) {

        binder.bind(ISimulationModel.class)
                .to(ConfigurableSimulationModel.class)
                .in(Singleton.class);
        binder.bind(ITraitDimension.class)
                .toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(UnstructuredMultidimensionalTraitAgentProvider.class);
        binder.bind(ITrait.class)
                .toProvider(RealTraitProvider.class);
        binder.bind(IAgentTag.class)
                .toProvider(SimpleAgentTagProvider.class);
        binder.bind(IPopulation.class).toProvider(SimpleAgentPopulationProvider.class);
        binder.bind(IInteractionTopology.class).toProvider(WellMixedInteractionTopologyProvider.class);
        binder.bind(IDeme.class).toProvider(SimpleAgentDemeProvider.class);
        binder.bind(ILogFiles.class).to(LogFileHandler.class).in(Singleton.class);
    }
}
