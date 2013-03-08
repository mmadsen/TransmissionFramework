/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/27/13
 * Time: 3:47 PM
 */

public interface IInitialPopulationBuilder {
    /**
     * Any class which implements this method can be used to initialize a simulation.  The goal here is that
     * the framework provides a couple of common builders (e.g., to initialize N agents with random traits,
     * perhaps across random demes) but a given model might need something specialized, which would be written
     * by the modeler.
     * <p/>
     * Your code can do anything here.  Run an algorithm, look stuff up in config files or a database, etc.
     * Just hand back an initialized population of agents with traits, any demes or structure tags, etc.
     *
     * @return population
     */

    public IPopulation constructInitialPopulation();
}
