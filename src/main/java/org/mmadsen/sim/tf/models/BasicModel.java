package org.mmadsen.sim.tf.models;

import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicModel implements ISimulationModel
{
    public BasicModel() {
    }

    public Integer getCurrentModelTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Integer getCurrentPopulationSize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
