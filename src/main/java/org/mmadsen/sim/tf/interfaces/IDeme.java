/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.interfaces;

import org.apache.commons.collections.Predicate;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 26, 2010
 * Time: 8:57:34 AM
 */

public interface IDeme {
    /**
        * Returns a typed List of agents currently in the population.  This list is a shallow
        * clone of the internal list of agents, so modifying the returned list does not affect
        * the underlying population's list of agents.
        * @return
        */
       List<IAgent> getAgents();

       /**
        * Shorthand query interface, returning a subpopulation of agents which currently possess the
        * given tag.  This is implemented internally by creating a Predicate which returns TRUE if
        * an agent possesses a given tag, and then passing this predicate to getSubpopulationMatchingPredicate().
        * @param tag
        * @return
        * @see #getSubpopulationMatchingPredicate
        *
        */
       IDeme getSubpopulationForTag(IAgentTag tag);

       /**
        * Returns one agent from the given population, chosen at random from a uniform
        * distribution.
        *
        * @return agent
        */
       IAgent getAgentAtRandom();


       /**
        * Query interface, returning an IPopulation instance which is populated by the agents for which
        * a Predicate (whether simple or compound/chained) returns TRUE.
        *
        * @param pred
        * @return subpopulation
        */
       IDeme getSubpopulationMatchingPredicate(Predicate pred);

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
