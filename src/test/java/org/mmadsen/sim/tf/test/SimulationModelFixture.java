/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.test;

import com.google.inject.Singleton;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;
import org.mmadsen.sim.tf.models.AbstractSimModel;
import com.google.common.base.Preconditions;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 3:58:10 PM
 * To change this template use File | Settings | File Templates.
 */

@Singleton
public class SimulationModelFixture extends AbstractSimModel {

    private Integer currentTime = 0;

    public SimulationModelFixture() {
        super();
        Preconditions.checkNotNull(log);
        
    }

    /**
     * Only valid in unit testing mode.  In a real simulation model we'd need to track time...
     * @return time  Integer representing the model's 'tick' time.
     */
    public Integer getCurrentModelTime() {
        currentTime++;
        return currentTime;
    }



    public void testSetCurrentPopulationSize(Integer popsize) {
        this.populationSize = popsize;
    }

    public void run() {
        log.info("Starting simulation model: " + this.getClass().getSimpleName());

    }
}
