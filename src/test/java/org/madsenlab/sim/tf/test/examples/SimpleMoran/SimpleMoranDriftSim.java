/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.SimpleMoran;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 9:55:24 AM
 */

public class SimpleMoranDriftSim {

    int numAgents = 200;
    @Inject
    public Provider<ITrait> traitProvider;
    @Inject
    public Provider<ITraitDimension> dimensionProvider;
    @Inject
    public Provider<IAgentTag> tagProvider;
    @Inject
    public ISimulationModel model;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleMoranDriftModule());
        ISimulationModel model = injector.getInstance(ISimulationModel.class);

        model.initializePopulation();

        /* Start the simulation model */
        model.run();
    }


}
