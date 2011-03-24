/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

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
     * @return time Returns the current model "tick"
     */
    public Integer getCurrentModelTime();

    /**
     * Returns a Logger configured with the centralized log4j config, for the calling class.
     *
     * @param classToLog Class to use for the returned Logger
     * @return logger  Logger for the calling class, properly configured given log4j config.
     */
    public Logger getModelLogger(Class classToLog);

    /**
     * Initializes the main random number generator for this simulation model run.  All
     * random numbers generated, from whatever distribution, are derived from an underlying
     * Mersenne Twister object.
     * <p/>
     * For debugging, validation, and unit testing, it can be useful to get a reproducible
     * stream of pseudorandom numbers.  Setting the boolean parameter to TRUE will initialize
     * the Mersenne Twister with a hardcoded constant seed, thus ensuring that the underlying
     * generator creates the same stream every time.  Note that this does NOT necessarily
     * guarantee the same outcome in a simulation run, unless everything else (such as external
     * input) is also held constant.
     *
     * @param reproducibleStream
     */
    public void initializeRNG(Boolean reproducibleStream);

    /**
     * Creates and initializes the population for a simulation, in preparation for starting a simulation
     * run.
     */

    public void initializeModel();

    /**
     * Creates and initializes any Guice Provider objects needed by the simulation model
     */

    public void initializeProviders();


    public IPopulation getPopulation();

    public IInteractionTopology getInteractionTopology();

    /**
     * Returns a random integer between 0 and ceiling, from a Uniform distribution
     * using the underlying Mersenne Twister generator.  Heavily used, for example,
     * in selecting a random agent in the population.
     *
     * @param ceiling
     * @return randomInt
     */
    public Integer getUniformRandomInteger(Integer ceiling);

    public Provider<IDeme> getDemeProvider();

    public Provider<ITrait> getTraitProvider();

    public Provider<ITraitDimension> getTraitDimensionProvider();

    public void parseCommandLineOptions(String[] args);

    public void incrementModelTime();

    public void modelStep();

    public void modelObservations();
}
