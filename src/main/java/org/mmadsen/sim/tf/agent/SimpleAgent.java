/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.agent;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 11, 2010
 * Time: 12:33:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleAgent implements IAgent {
    private String agentID;
    private ISimulationModel model;
    private Logger log;

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        log.debug("setSimulationModel called and agent object initialized");
    }

    public String getAgentID() {
        return this.agentID;
    }

    public void setAgentID(String id) {
        this.agentID = id;
    }

    public void adoptTrait(ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void adoptTrait(ITraitDimension dimension, ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addTraitDimension(ITraitDimension dimension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
