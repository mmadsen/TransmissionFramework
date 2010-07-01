package org.mmadsen.sim.tf.interfaces;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 29, 2010
 * Time: 4:36:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IModelGlobals {

    /**
     *
     * @return time Returns the current model "tick"
     */
    public Integer getCurrentModelTime();

    public Logger getModelLogger();


}
