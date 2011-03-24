/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IInteractionRule;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 25, 2010
 * Time: 4:20:49 PM
 */

public abstract class AbstractInteractionRule implements IInteractionRule {
    protected ISimulationModel model;
    protected Logger log;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleDescription() {
        return this.ruleDescription;
    }

    protected String ruleName;
    protected String ruleDescription;


    public void execute(Object o) {
        this._preExecution();
        ruleBody(o);
        this._postExecution();
    }

    protected void _preExecution() {
        log.trace("Entering _preExecution on rule " + this.getRuleName());
    }

    protected void _postExecution() {
        log.trace("Entering _postExecution on rule " + this.getRuleName());
    }

    public abstract void ruleBody(Object o);

}
