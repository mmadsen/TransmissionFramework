/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.IStatisticsSubject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:44 AM
 */

public interface IClass extends IStatisticsSubject {

    /**
     * Primary key for the class within the classification, since class descriptions are meant to be
     * human readable summaries of the underlying dimension/modes.
     * <p/>
     * This is not changeable in the public API, and is set by constructor.
     *
     * @return Integer
     */

    public Integer getClassIDWithinClassification();

    /**
     * @return modes - A List of IClassDimensionModes representing the definition of this class as a
     *         combination of modes along N different dimensions.
     */

    public Set<IClassDimensionMode> getModesDefiningClass();

    /**
     * @return modeDescriptions - A string representing the textual description of all modes making up this class
     */

    public String getClassDescription();

    /**
     * @return count An integer representing the current number of IAgent objects which have adopted this trait
     */

    public int getCurrentAdoptionCount();

    /**
     * Since adoption is counted globally and per IAgentTag, this method returns the
     * current adoption count for each IAgentTag belonging to an agent who currently holds
     * the trait.
     *
     * @return tagCountMap  A map of counts, with IAgentTag keys
     */

    public Map<IAgentTag, Integer> getCurrentAdoptionCountsByTag();


    public Integer getCurrentAdoptionCountForTag(IAgentTag tag);

    /**
     * Registers an {@link org.madsenlab.sim.tf.interfaces.IAgent} object as adopting this ITrait and any child traits.  Increments the trait's adoption count,
     * in a thread-safe way, along with the adoption count of any child traits.
     *
     * @param agent an IAgent object, representing the agent adopting this trait
     */
    public void adopt(IAgent agent);

    /**
     * Unregisters an {@link IAgent} object from this trait and its child traits.  Decrements the trait's adoption count,
     * in a thread-safe way, along with the adoption count of any child traits.
     *
     * @param agent an IAgent object, representing the agent unregistering this trait
     */

    public void unadopt(IAgent agent);

    /**
     * @return agentList A List of IAgent objects representing agents that current have adopted this trait.
     */


    public List<IAgent> getCurrentAdopterList();


    // TODO:  Need to think carefully about class duration and whether we can consider zero an "exit"...perhaps keep track of all zero crossing times

    /**
     * Returns true if the trait has zero adoptees and has been "lost" from the population, and thus
     * a total duration can be calculated.  If this returns false for a given trait, the caller should assume
     * that the trait has a non-zero adoption count and thus is still "in play" within the population, so asking for its
     * duration would skew any statistics.
     */
    public Boolean hasCompleteDuration();

    /**
     * Returns the number of model ticks (copying events in Moran-style models, or N * tick copying events in WF models)
     * the trait persisted in the population at non-zero adoption levels before the adoption count first hit zero.
     *
     * @return number of model ticks the trait persisted in the population before being lost
     */
    public Integer getTraitDuration();

    /**
     * We often want to index classes for fast lookup given a set of IClassDimensionModes.  This method returns the hashcode()
     * value for the underlying Set<IClassDimensionMode> which makes up the class.  This value should always match
     * the value one obtains by hashcoding a set of the relevant modes.
     *
     * @return hashcode
     */

    public int getUniqueID();

    /**
     * Compares class descriptions, to get a consistent ordering in log files
     *
     * @return integer
     */

    public int compareTo(IClass otherClass);
}
