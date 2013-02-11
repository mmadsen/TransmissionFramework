/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/24/11
 * Time: 2:39 PM
 */

public class CopyConformistTraitNeighborRule extends AbstractInteractionRule implements ICopyingRule, IConformistCopyingRule {
    TraitCopyingMode mode = TraitCopyingMode.CURRENT;    // default to Moran model style copying
    Boolean antiConformismFlag = false;

    public CopyConformistTraitNeighborRule(ISimulationModel m) {
        model = m;
        this.log = model.getModelLogger(this.getClass());
        this.setRuleName("RCMNeighborSD");
        this.setRuleDescription("Randomly Copy a Neighbor's Trait from a Single Dimension");
        this.antiConformismFlag = this.model.getModelConfiguration().getAntiConformist();
        log.info("Conformism Mode - anticonformism is " + this.antiConformismFlag);
    }

    public void setTraitCopyingMode(TraitCopyingMode m) {
        this.mode = m;
    }

    public synchronized void ruleBody(Object o) {
        log.debug("entering rule body for: " + this.getRuleName());
        IAgent thisAgent = (IAgent) o;

        // Get a random neighbor.  First, we need the interaction topology
        IInteractionTopology topology = model.getInteractionTopology();
        IDeme neighborAgents = topology.getNeighborsForAgent(thisAgent);

        ITrait newTrait;
        if (this.antiConformismFlag == true) {
            // Determine the least frequent trait among neighbors
            newTrait = neighborAgents.getLeastFrequentTrait(this.mode);
        } else {
            // Determine the most frequent trait among neighbors
            newTrait = neighborAgents.getMostFrequentTrait(this.mode);
        }

        ITrait focalTrait = thisAgent.getRandomTraitFromAgent(this.mode);
        log.trace("focal agent unadopting trait: " + focalTrait.getTraitID());
        log.trace("focal agent adopting most freq trait: " + newTrait.getTraitID());

        // Now unadopt the existing trait from thisAgent, and adopt the neighbor's random trait
        // shortcircuit if the traits are the same
        if (!focalTrait.getTraitID().equals(newTrait.getTraitID())) {
            focalTrait.unadopt(thisAgent);
            newTrait.adopt(thisAgent);
        } else {
            log.trace("traits the same, not copying");
        }
    }

    public void registerSubRule(IActionRule rule) {
        // null in this implementation

    }

    public void deregisterSubRule(IActionRule rule) {
        // null in this implementation

    }
}
