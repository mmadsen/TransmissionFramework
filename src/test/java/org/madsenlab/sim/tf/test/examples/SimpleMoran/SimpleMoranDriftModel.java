/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.SimpleMoran;

import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.ITraitStatisticsObserver;
import org.madsenlab.sim.tf.models.AbstractSimModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 10:19:32 AM
 */

public class SimpleMoranDriftModel extends AbstractSimModel {
    ITraitStatisticsObserver<ITraitDimension> obs;

    // TODO:  What to fill out in the simulation model
    // Set up a trait dimension
    // Set up an initial set of traits
    // Set up an initial set of agents
    // Randomly assign initial set of traits to initial agents
    // Set up an IPopulation for the agents, assume well-mixed
    //


    private void setup() {
        this.obs = new TraitCountObserver(this);
    }

    public void run() {

    }


}
