/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.models;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IModelDynamics;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.enums.GenerationDynamicsMode;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/27/13
 * Time: 8:18 AM
 */

public class MoranDynamics implements IModelDynamics {
    private ISimulationModel model;
    private Logger log;
    private GenerationDynamicsMode mode;

    @Override
    public GenerationDynamicsMode getGenerationDynamicsMode() {
        return mode;
    }

    public MoranDynamics(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.mode = GenerationDynamicsMode.CONTINUOUS;
    }


    @Override
    public void modelStep() {
        log.trace("entering modelStep at time: " + this.model.getCurrentModelTime());
        // pick a random agent, and fire its rules stack....
        IAgent focalAgent = this.model.getPopulation().getAgentAtRandom();
        log.trace("agent " + focalAgent.getAgentID() + " - firing rules");
        focalAgent.fireRules();
        // Now do any observers or observation on the results of this transmission step
        List<ITraitDimension> dimList = this.model.getTraitDimensions();
        for (ITraitDimension dim : dimList) {
            dim.notifyObservers();
        }
    }
}
