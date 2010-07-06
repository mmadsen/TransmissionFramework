package org.mmadsen.sim.tf.test;

import com.google.inject.Singleton;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
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

    static Integer currentTime = 0;
    static Integer curPopSize = 0;

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

    public Logger getModelLogger() {
        return log;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Integer getCurrentPopulationSize() {
        return curPopSize;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void testSetCurrentPopulationSize(Integer popsize) {
        curPopSize = popsize;
    }
}
