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
 * IInteractionTopology specifies the methods required for classes implementing
 * non-well-mixed population "structure," whether explicitly spatial or arbitrary
 * interaction weightings.
 * <p/>
 * Two sets of methods are needed.  "Local" methods specify operations from the point
 * of view of a specific agent or agent set, such as determining "nearby" agents or
 * the set of agents to whom one is linked by social network ties.
 * <p/>
 * "Global" methods specify operations and observables across the entire population
 * structure.  Examples include calculating aspects of the degree distribution for
 * the population, or "distance" statistics between agents.
 * <p/>
 * <p/>
 * User: mark
 * Date: Oct 7, 2010
 * Time: 7:28:28 PM
 */

public interface IInteractionTopology {

    /* **********************************
      Topologically "local" methods 
     ***********************************/
    IAgent getRandomNeighborForAgent(IAgent focalAgent);

    IDeme getNeighborsForAgent(IAgent focalAgent);

    /* **********************************
      Topologically "global" methods
     ***********************************/

    // Degree distribution statistics
    //Double getMeanNeighborDegree();

    //Double getVarianceNeighborDegree();

    // The following allows arbitrary moment calculations
    // if arg = 1, returns getMeanNeighborDegree,
    // if arg = 3, returns third central moment
    //Double getMomentNeighborDegree(int moment);

    // Distance metrics
    // Does not assume that the underlying topology is a graph but
    // reduces to min/max path length if it is a graph
    //Double getMinGeodesicDistance();

    //Double getMaxGeodesicDistance();

}
