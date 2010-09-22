/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IPopulation;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 10, 2010
 * Time: 2:08:04 PM
 */

public class SimpleAgentPopulationProvider implements Provider<IPopulation> {
    @Inject
    public ISimulationModel model;
    @Inject
    public Provider<IAgent> agentProvider;

    public IPopulation get() {
        SimpleAgentPopulation pop = new SimpleAgentPopulation();
        pop.initialize(model, agentProvider);
        return pop;
    }
}