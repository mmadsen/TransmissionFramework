/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.structure;

import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.AgentTagType;

import java.util.List;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 12/11/10
 * Time: 3:19 PM
 */

public class WellMixedWithinDemeTopology implements IInteractionTopology {
    IPopulation population;
    ISimulationModel model;

    public void initialize(ISimulationModel model) {
        this.model = model;
        this.population = this.model.getPopulation();
    }

    // In a well-mixed interaction topology, we really don't care about the focalAgent, we just
    // ensure that we're returning a random agent *other than* the focal agent.

    public IAgent getRandomNeighborForAgent(IAgent focalAgent) {
        IDeme deme = this.getNeighborsForAgent(focalAgent);
        return deme.getAgentAtRandom();
    }

    // Return a new IDeme object with all agents except the focal agent, since this
    // is a well-mixed model, but from a deme of agents restricted to a single tag representing the deme

    public IDeme getNeighborsForAgent(IAgent focalAgent) {
        Set<IAgentTag> agentTags = focalAgent.getAgentTagsMatchingType(AgentTagType.DEME);
        IAgentTag demeTag = agentTags.iterator().next();
        // TODO: bit of hack, hopefully we only have one set of Deme tags, and one per agent...so this won't work if we have nested demes etc etc.

        IDeme deme = this.population.getDemeForTag(demeTag);
        IDeme allButFocalDeme = this.model.getDemeProvider().get();

        List<IAgent> agentList = deme.getAgents();
        agentList.remove(focalAgent);
        allButFocalDeme.setAgentList(agentList);

        return allButFocalDeme;
    }

}
