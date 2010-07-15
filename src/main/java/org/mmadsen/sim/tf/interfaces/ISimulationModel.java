/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.interfaces;

import com.google.inject.Provider;
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

    public IAgent createAgent();

    public void removeAgent(IAgent agent);

    public Provider<ITrait> getTraitProvider();

    public Provider<ITraitDimension> getTraitDimensionProvider();

}
