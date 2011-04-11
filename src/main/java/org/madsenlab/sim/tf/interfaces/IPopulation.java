/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

/**
 * IPopulation is the full collection of agents in a simulation model.  Classes implementing
 * IPopulation are the locus of agent creation, tracking, and destruction in a model.
 * <p/>
 * Subpopulations in TransmissionFramework are called "demes," and are not represented by persistent
 * objects but rather are "projections" of the population (by tag, by geography, by trait group, etc).
 * Most operations one would routinely perform on a group of agents (e.g., selecting a random agent,
 * selecting a subset of agents which have a particular trait or tag) are done through the IDeme
 * abstraction.  IPopulation extends IDeme and adds agent creation and destruction.
 * <p/>
 * <p/>
 * User: mark
 * Date: Aug 21, 2010
 * Time: 3:06:05 PM
 */

public interface IPopulation extends IDeme {


    /**
     * Creates a newly initialized agent, using the underlying agentProvider (thus automatically
     * creating whatever agent implementation is being used in this simulation model).  The new agent
     * object is added to the population list, the population size is incremented, and any population
     * structure (e.g., network, lattice) object is notified and passed the object for insertion into
     * the population structure.  Following this, the new agent is returned to the caller.
     *
     * @return agent
     */
    public IAgent createAgent();

    /**
     * Creates a newly initialized agent, using {@link createAgent}, and tags the agent with the provided
     * tag.  Following this, the new agent is returned to the caller.
     */
    public IAgent createAgentWithTag(IAgentTag tag);

    /**
     * Removes a specified agent from the population (allowing simulation of death or outmigration,
     * for example).  Any population structure object registered with this population is notified, so
     * that the agent can be removed (e.g., vertex and links in a network removed), the population size
     * is decremented, and the agent object is removed from the population list.
     * <p/>
     * Simulation code should not hold persistent references to individual agent objects for a duration
     * longer than a simulation step, because those references might end up invalid, or pointing to
     * and agent object which no longer belongs to a population and thus is not "part" of the model anymore.
     *
     * @param agent
     */
    public void removeAgent(IAgent agent);

    public void clearAgentPopulation();
}
