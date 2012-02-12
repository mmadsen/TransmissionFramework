/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;


import gnu.trove.map.hash.TIntIntHashMap;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 27, 2010
 * Time: 11:26:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ITrait extends IStatisticsSubject {

    public void setSimulationModel(ISimulationModel m);

    /**
     * @return idString A string representation of the ITrait including any dependent child traits
     */
    public String getTraitID();

    public void setTraitID(String id);

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
     * Returns a List of adoption counts for the lifetime of the simulation run thus far.
     *
     * @return countList A list of adoption counts for the lifetime of the simulation to date
     */
    public TIntIntHashMap getAdoptionCountHistory();


    /**
     * Registers an {@link IAgent} object as adopting this ITrait and any child traits.  Increments the trait's adoption count,
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

    /**
     * Returns the ITraitDimension, if any, to which this trait belongs.  Can return null
     * if this trait doesn't belong to a dimension (there may be models which do not use
     * ITraitDimensions at all.
     *
     * @return dimension  ITraitDimension to which this trait belongs (may be null)
     */


    public ITraitDimension getOwningDimension();

    /**
     * Sets the ITraitDimension which "owns" this trait.  Many simple models may not employ
     * trait dimensions at all, since they are primarily useful in organizing "alternative"
     * traits in models with multidimensional state spaces.
     *
     * @param owningDimension
     */

    public void setOwningDimension(ITraitDimension owningDimension);


    /**
     * Clears adoption data and resets adoption count to zero
     */

    public void clearAdoptionData();

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

}
