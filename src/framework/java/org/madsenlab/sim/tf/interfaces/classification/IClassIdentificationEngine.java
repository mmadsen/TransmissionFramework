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
import org.madsenlab.sim.tf.interfaces.IPopulation;
import org.madsenlab.sim.tf.interfaces.ITrait;

import java.util.Set;

/**
 * IClassIdentificationEngine represents the API for identifying the IClass within an IClassification to which a given
 * IAgent belongs, given their current set of traits.
 * <p/>
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:45 AM
 */

public interface IClassIdentificationEngine {
    /**
     * Given an agent, return the class implied
     *
     * @param agent
     * @return
     */
    public IClass getClassForAgent(IAgent agent);

    /**
     * Given a set of traits, return the class implied.  This is probably used mainly for testing purposes
     *
     * @param traitSet
     * @return
     */

    public IClass getClassForTraits(Set<ITrait> traitSet);

    /**
     * Given previous and currently adopted traits for a particular agent, determines if an agent has
     * "changed class" given its traits, and if so, it does the class unadopt/adopt pair to update class
     * counts.
     * <p/>
     * This method is one of the main usages of this interface, and should be used to update system state after every
     * trait adopt event.
     */

    public void updateClassForAgent(IAgent agent);

    /**
     * Given an IPopulation object, determines for each agent if a class has changed, and if so, executes
     * the appropriate class unadopt/adopt pair to update class adoption counts.
     * <p/>
     * This method is one of the main usages of this interface, and for many types of models will be the method
     * used after each model step but before notifying classification observers to record class counts.
     */

    public void updateClassForPopulation(IPopulation pop);


}
