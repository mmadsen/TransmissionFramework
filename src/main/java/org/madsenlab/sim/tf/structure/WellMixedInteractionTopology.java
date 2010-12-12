/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.structure;

import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.AllAgentsPredicate;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 12/11/10
 * Time: 3:19 PM
 */

public class WellMixedInteractionTopology implements IInteractionTopology {
    IPopulation population;
    ISimulationModel model;

    public void initialize(ISimulationModel model) {
        this.model = model;
        this.population = this.model.getPopulation();
    }

    // In a well-mixed interaction topology, we really don't care about the focalAgent, we just
    // ensure that we're returning a random agent *other than* the focal agent.

    public IAgent getRandomNeighborForAgent(IAgent focalAgent) {
        IDeme deme = this.population.getDemeMatchingPredicate(new AllAgentsPredicate());
        IAgent chosenAgent = null;
        do {
            chosenAgent = deme.getAgentAtRandom();

        } while(!chosenAgent.equals(focalAgent));

        return chosenAgent;
    }

    public IDeme getNeighborsForAgent(IAgent focalAgent) {
        return null;
    }
}
