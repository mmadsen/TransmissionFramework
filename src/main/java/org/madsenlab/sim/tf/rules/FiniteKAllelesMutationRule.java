/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.config.GlobalModelConfiguration;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/25/11
 * Time: 11:13 AM
 */

public class FiniteKAllelesMutationRule extends AbstractInteractionRule {
    Double mutationRate;

    public FiniteKAllelesMutationRule(ISimulationModel m, GlobalModelConfiguration params) {
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


        // Generate a random double between 0 and 1, if this value is less than the mutation rate,
        // a mutation "event" has occurred.  If not, the rule body does nothing.
        Double chance = this.model.getUniformDouble();
        if( chance < this.mutationRate ) {


        }

    }
}
