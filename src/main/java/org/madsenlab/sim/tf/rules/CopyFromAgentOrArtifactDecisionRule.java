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

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 4/11/11
 * Time: 10:15 AM
 */

public class CopyFromAgentOrArtifactDecisionRule extends AbstractInteractionRule {
    private List<IActionRule> copyingRuleList;
    private List<IActionRule> mutationRuleList;
    private Double mutationRate;

    public CopyFromAgentOrArtifactDecisionRule(ISimulationModel m) {
        this.model = m;
        this.log = model.getModelLogger(this.getClass());

        this.copyingRuleList = new ArrayList<IActionRule>();
        this.mutationRuleList = new ArrayList<IActionRule>();

        this.setRuleName("CopyFromAgentOrArtifactDecisionRule");
        this.setRuleDescription("Ensure that copying from an agent or an artifact tuple happens in a single time step in a continuous-time simulation");
        this.mutationRate = this.model.getModelConfiguration().getMutationRate();
    }

    public void ruleBody(Object o) {
        log.trace("entering rule body for: " + this.getRuleName());

        /*// Generate a random double between 0 and 1, if this value is less than the mutation rate,
        // a mutation "event" has occurred.  If not, the rule body does nothing.
        Double draw = this.model.getUniformDouble();
        if( draw < this.mutationRate ) {
            // fire the mutation rule stack
            log.debug("mutation occurred with random draw: " + draw + " < rate: " + this.mutationRate);
            for(IInteractionRule rule: this.mutationRuleList) {
                rule.execute(o);
            }
        }
        else {
            // fire the copying rule stack
            log.debug("no mutation - firing copying rule stack");
            for(IInteractionRule rule: this.copyingRuleList) {
                rule.execute(o);
            }
        }*/
    }

    public void registerSubRule(IActionRule rule) {
        if(rule instanceof ICopyingRule) {
            this.copyingRuleList.add(rule);
        }
        else if( rule instanceof IMutationRule) {
            this.mutationRuleList.add(rule);
        }
        else {
            log.error("FATAL: Rule object being registered in CopyFromAgentOrArtifactDecisionRule of unknown rule type");
            System.exit(1);
        }

    }

    public void deregisterSubRule(IActionRule rule) {
        if(rule instanceof ICopyingRule) {
            this.copyingRuleList.remove(rule);
        }
        else if( rule instanceof IMutationRule) {
            this.mutationRuleList.remove(rule);
        }
    }
}
