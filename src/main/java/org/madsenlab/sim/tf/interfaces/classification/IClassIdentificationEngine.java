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
import org.madsenlab.sim.tf.interfaces.ITraitDimension;

import java.util.Map;

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
     * Given a set of traits in trait dimensions, return the class implied.
     *
     * @param traitMap
     * @return
     */


}
