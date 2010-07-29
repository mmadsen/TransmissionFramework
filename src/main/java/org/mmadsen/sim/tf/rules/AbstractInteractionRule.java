/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.rules;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.InteractionRule;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 25, 2010
 * Time: 4:20:49 PM
 */

public abstract class AbstractInteractionRule implements InteractionRule {
    protected ISimulationModel model;
    protected Logger log;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    protected String ruleName;

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
    }


    public void execute(Object o) {
        this._preExecution();
        ruleBody(o);
        this._postExecution();
    }

    protected void _preExecution() {
        log.info("Entering _preExecution on rule " + this.getRuleName());
    }

    protected void _postExecution() {
        log.info("Entering _postExecution on rule " + this.getRuleName());
    }

    public abstract void ruleBody(Object o);

}
