/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.*;

import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/24/11
 * Time: 2:39 PM
 */

public class RandomCopyNeighborSingleDimensionRule extends AbstractInteractionRule implements ICopyingRule {

    public RandomCopyNeighborSingleDimensionRule(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        this.setRuleName("RCMNeighborSD");
        this.setRuleDescription("Randomly Copy a Neighbor's Trait from a Single Dimension");
    }

    public void ruleBody(Object o) {
        log.debug("entering rule body for: " + this.getRuleName());
        IAgent thisAgent = (IAgent) o;

        // Get a random neighbor.  First, we need the interaction topology
        IInteractionTopology topology = model.getInteractionTopology();
        IAgent neighborAgent = topology.getRandomNeighborForAgent(thisAgent);

        log.trace("focal agent: " + thisAgent.getAgentID() + " <=> neighbor: " + neighborAgent.getAgentID());


        Set<ITrait> neighborTraitSet = neighborAgent.getCurrentlyAdoptedTraits();
        ITrait neighborTrait = this.getRandomTraitFromAgent(neighborAgent);
        log.trace("neighbor has trait: " + neighborTrait.getTraitID());

        ITrait focalTrait = this.getRandomTraitFromAgent(thisAgent);
        log.trace("focal agent has trait: " + focalTrait.getTraitID());

        // Now unadopt the existing trait from thisAgent, and adopt the neighbor's random trait
        focalTrait.unadopt(thisAgent);
        neighborTrait.adopt(thisAgent);

    }

    public void registerSubRule(IActionRule rule) {
        // null in this implementation

    }

    public void deregisterSubRule(IActionRule rule) {
        // null in this implementation

    }
}