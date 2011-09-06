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

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/25/11
 * Time: 11:13 AM
 */

public class FiniteKAllelesMutationRule extends AbstractInteractionRule implements IMutationRule {
    Double mutationRate;

    public FiniteKAllelesMutationRule(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        this.setRuleName("FiniteKAllelesMutation");
        this.setRuleDescription("Randomly mutate trait to ");
    }

    public void setMutationRate(Double mutation) {
        this.mutationRate = mutation;
    }

    public void ruleBody(Object o) {
        log.trace("entering rule body for: " + this.getRuleName());
        IAgent thisAgent = (IAgent) o;

        // DECISION TO MUTATE OR COPY IS MADE IN ANOTHER RULE - IF THIS RULE FIRES AT ALL, IT JUST NEEDS
        // TO PERFORM THE RIGHT KIND OF MUTATION

    }

    public void registerSubRule(IActionRule rule) {
        // null in this implementation
    }

    public void deregisterSubRule(IActionRule rule) {
        // null in this implementation
    }
}
