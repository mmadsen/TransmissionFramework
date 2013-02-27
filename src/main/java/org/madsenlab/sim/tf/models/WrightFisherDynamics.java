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

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/27/13
 * Time: 8:03 AM
 */

public class WrightFisherDynamics implements IModelDynamics {
    private ISimulationModel model;
    private Logger log;

    public WrightFisherDynamics(ISimulationModel m) {
        this.model = m;
        this.log = Logger.getLogger(this.getClass());
    }


    @Override
    public void modelStep() {
        log.trace("========================== STEP: " + this.model.getCurrentModelTime() + "============================");

        // In order to ensure that we do not create numerical artifacts by using a fixed order of enumeration
        // we shuffle the population before we step through it.
        List<IAgent> shuffledAgentList = this.model.getPopulation().getAgentsShuffledOrder();

        // In WF model, each of the N copying events in an elemental step must be done with replacement
        // which means that *trait selection* must be done on a copy of the population made at the beginning
        // of each step, and trait adoption/unadoption will then form the new state of the population, which
        // is Observed.
        this.model.getPopulation().savePreviousStepTraits();

        // Now iterate over all agents in the population and fire their rules, forming a new sample of the
        // previous step's trait distribution
        for (IAgent agent : shuffledAgentList) {
            agent.fireRules();
        }

        // Now do any observers or observation on the results of this transmission step
        List<ITraitDimension> dimList = this.model.getTraitDimensions();
        for (ITraitDimension dim : dimList) {
            dim.notifyObservers();
        }

        // Now run through classification identification engines, and update class membership

        // Now notify all the classification observers....


        //log.trace("exiting modelStep at time: " + this.model.getCurrentModelTime());
    }
}
