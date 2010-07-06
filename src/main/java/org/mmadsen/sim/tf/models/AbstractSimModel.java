package org.mmadsen.sim.tf.models;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSimModel implements ISimulationModel
{
    protected Logger log;
    static Integer currentTime = 0;

    public AbstractSimModel() {
        PropertyConfigurator.configure("/Users/mark/Documents/src/TransmissionFramework/src/main/resources/log4j.config");
        log = Logger.getLogger(this.getClass());
        log.info("log4j configured and ready");
    }

    public Integer getCurrentModelTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger(Class classToLog) {
        return Logger.getLogger(classToLog);
    }

    public Integer getCurrentPopulationSize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void run() {
        log.info("Starting simulation model: " + this.getClass().getSimpleName());
    }
}
