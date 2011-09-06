/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.util;

import com.google.common.base.Preconditions;
import org.junit.Ignore;
import org.madsenlab.sim.tf.models.AbstractSimModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 3:58:10 PM
 * To change this template use File | Settings | File Templates.
 */

//@Ignore needed to prevent JUnit from trying to execute test helper classes

@Ignore
public class SimulationModelFixture extends AbstractSimModel {

    public void initializeModel() {

    }

    public SimulationModelFixture() {
        super();
        Preconditions.checkNotNull(log);

    }

    /**
     * Only valid in unit testing mode.  In a real simulation model we'd need to track time...
     *
     * @return time  Integer representing the model's 'tick' time.
     */
    public Integer getCurrentModelTime() {
        currentTime++;
        return currentTime;
    }

    public void parseCommandLineOptions(String[] args) {

    }


    public void run() {
        log.info("Starting simulation model: " + this.getClass().getSimpleName());

    }

    public void modelFinalize() {

    }

    public void modelObservations() {

    }

    public void modelStep() {

    }
}
