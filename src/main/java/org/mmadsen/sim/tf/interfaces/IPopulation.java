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

import java.util.Collection;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 21, 2010
 * Time: 3:06:05 PM
 */

public interface IPopulation {

    List<IAgent> getAgents();

    // Could do this with predicates, but tags are self-tracking so they actually
    // know their agent population already
    IPopulation getSubpopulationForTag(IAgentTag tag);

    // returns one IAgent at random given Uniform distribution
    IAgent getAgentAtRandom();

    // Finder interface - returns agents for which a Predicate (simple or compound) returns TRUE
    // Useful for finding arbitrary subsets of agents besides tag sets or graph neighbor sets
    IPopulation getSubpopulationMatchingPredicate(Predicate pred);

    public Integer getCurrentPopulationSize();

    public IAgent createAgent();

    public void removeAgent(IAgent agent);

    public void clearAgentPopulation();
}
