package org.mmadsen.sim.tf.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.IModelGlobals;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 3:58:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelFixture implements IModelGlobals {

    static Logger logger;
    static Integer currentTime = 0;

    public ModelFixture() {
        this.logger = Logger.getLogger(ModelFixture.class.getName());
        this.logger.setLevel(Level.INFO);
        BasicConfigurator.configure();
    }

    public Integer getCurrentModelTime() {
        currentTime++;
        return currentTime;
    }

    public Logger getModelLogger() {
        return logger;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
