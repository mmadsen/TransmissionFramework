package org.mmadsen.sim.tf.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 27, 2010
 * Time: 11:26:38 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ITrait {


    /**
     *
     * @return idString A string representation of the ITrait including any dependent child traits
     */
    public String getTraitID();

    /**
     * 
     * @return count An integer representing the current number of IAgent objects which have adopted this trait
     */

    public Integer getCurrentAdoptionCount();


    /**
     * Returns a List of adoption counts for the lifetime of the simulation run thus far.
     * @return countList A list of adoption counts for the lifetime of the simulation to date
     */
    public Map<Integer,Integer> getAdoptionCountHistory();
    

    /**
     * Registers an {@link IAgent} object as adopting this ITrait and any child traits.  Increments the trait's adoption count,
     * in a thread-safe way, along with the adoption count of any child traits.
     *
     * @param agentAdopting  an IAgent object, representing the agent adopting this trait
     *
     */
    public void adopt(IAgent agentAdopting);

    /**
     * Unregisters an {@link IAgent} object from this trait and its child traits.  Decrements the trait's adoption count,
     * in a thread-safe way, along with the adoption count of any child traits.
     *
     * @param agentUnadopting  an IAgent object, representing the agent unregistering this trait
     *
     */

    public void unadopt(IAgent agentUnadopting);

    /**
     *
     * @return agentList A List of IAgent objects representing agents that current have adopted this trait.
     */

    public List<IAgent> getCurrentAdopterList();

    /**
     * Clears adoption data and resets adoption count to zero
     *
     */

    public void clearAdoptionData();

}
