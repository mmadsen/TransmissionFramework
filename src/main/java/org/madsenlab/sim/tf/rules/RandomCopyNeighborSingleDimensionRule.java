/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/24/11
 * Time: 2:39 PM
 */

public class RandomCopyNeighborSingleDimensionRule extends AbstractInteractionRule {

    public RandomCopyNeighborSingleDimensionRule(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        this.setRuleName("RCMNeighborSD");
        this.setRuleDescription("Randomly Copy a Neighbor's Trait from a Single Dimension");
    }

    public void ruleBody(Object o) {
        log.debug("entering rule body for: " + this.getRuleName());

    }
}
