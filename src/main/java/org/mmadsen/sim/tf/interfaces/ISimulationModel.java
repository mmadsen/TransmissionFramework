package org.mmadsen.sim.tf.interfaces;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 29, 2010
 * Time: 4:36:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ISimulationModel extends Runnable {

    /**
     *
     * @return time Returns the current model "tick"
     */
    public Integer getCurrentModelTime();

    /**
     * Returns a Logger configured with the centralized log4j config, for the calling class.
     * @param classToLog  Class to use for the returned Logger
     * @return logger  Logger for the calling class, properly configured given log4j config.
     */
    public Logger getModelLogger(Class classToLog);

    /**
     * Returns the current size of the agent population for a given simulation instance.
     * This value is not assumed to be constant over the lifetime of a simulation run,
     * to allow models with population dynamics.  The value is guaranteed, however, to be
     * constant for a model "tick", following the usual stochastic model convention that
     * the probability of two events in the same infinitesimal interval is O(dt^2).
     *
     * @return popSize The number of individual agents in the simulated population at the current time
     */
    public Integer getCurrentPopulationSize();


}
