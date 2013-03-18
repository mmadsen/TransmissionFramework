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
import org.madsenlab.sim.tf.enums.TraitCopyingMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 4/11/11
 * Time: 10:15 AM
 */

public class ConformismOrRandomDecisionRule extends AbstractInteractionRule implements ICopyingRule {
    private List<IActionRule> conformismRuleList;
    private List<IActionRule> randomRuleList;
    private Double conformismRate;
    private TraitCopyingMode mode;
    private Integer numConformistEvents = 0;
    private Integer numRandomEvents = 0;
    private Map<String, String> parameterMap;

    public ConformismOrRandomDecisionRule(ISimulationModel m) {
        this.model = m;
        this.log = model.getModelLogger(this.getClass());

        this.conformismRuleList = new ArrayList<IActionRule>();
        this.randomRuleList = new ArrayList<IActionRule>();

        this.setRuleName("ConformismOrRandomDecisionRule");
        this.setRuleDescription("Ensure that copying by conformism versus random copying occurs at correct rate");

    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameterMap = parameters;
    }

    public void ruleBody(Object o) {
        log.trace("entering rule body for: " + this.getRuleName());
        this.conformismRate = Double.parseDouble(this.parameterMap.get("conformistfrequency"));

        Double draw = this.model.getUniformDouble();
        if (draw < this.conformismRate) {
            // fire the conformist copying rule stack
            this.numConformistEvents++;
            log.trace("conformist copying occurred with random draw: " + draw + " < rate: " + this.conformismRate);
            for (IActionRule rule : this.conformismRuleList) {
                rule.execute(o);
            }
        } else {
            log.trace("conformism not occuring this step - firing unbiased copying stack");
            this.numRandomEvents++;
            for (IActionRule rule : this.randomRuleList) {
                rule.execute(o);
            }
        }
        log.trace("num conformist: " + this.numConformistEvents + " num random: " + this.numRandomEvents);
    }


    public void registerSubRule(IActionRule rule) {
        if (rule instanceof IConformistCopyingRule) {
            this.conformismRuleList.add(rule);
        } else if (rule instanceof IUnbiasedCopyingRule) {
            this.randomRuleList.add(rule);
        } else {
            log.error("FATAL: Rule object being registered in ConformismOrRandomDecisionRule of unknown rule type" + rule.getRuleDescription());
            System.exit(1);
        }

    }

    public void deregisterSubRule(IActionRule rule) {
        if (rule instanceof IConformistCopyingRule) {
            this.conformismRuleList.remove(rule);
        } else if (rule instanceof IUnbiasedCopyingRule) {
            this.randomRuleList.remove(rule);
        }
    }

    @Override
    public void setTraitCopyingMode(TraitCopyingMode mode) {
        this.mode = mode;
    }
}
