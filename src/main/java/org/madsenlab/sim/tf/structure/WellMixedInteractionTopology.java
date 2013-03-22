/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
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
    ISimulationModel model;

    public WellMixedInteractionTopology(ISimulationModel model) {
        this.initialize(model);
    }

    public void initialize(ISimulationModel model) {
        this.model = model;
    }


    public IAgent getRandomNeighborForAgent(IAgent focalAgent) {
        IDeme deme = this.getNeighborsForAgent(focalAgent);
        return deme.getAgentAtRandom();
    }

    // Return a new IDeme object with all agents since there should be a probability of keeping one's current trait (ignore the param)

    public IDeme getNeighborsForAgent(IAgent focalAgent) {
        return this.model.getPopulation().getDemeMatchingPredicate(new AllAgentsPredicate());
    }
}
