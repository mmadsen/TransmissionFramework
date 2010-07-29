/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.interfaces;


import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 27, 2010
 * Time: 11:26:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IAgent {

    public void setSimulationModel(ISimulationModel m);

    public String getAgentID();

    public void setAgentID(String id);

    /* Trait Handling methods */

   /**
     * Adds a trait dimension to an agent
     *
     *
     *
     * @param dimension
     */

    public void addTraitDimension(ITraitDimension dimension);


    /* Trait adoption methods */

    public void adoptTrait(ITrait trait);

    public void adoptTrait(ITraitDimension dimension, ITrait trait);

    public void unadoptTrait(ITrait trait);

    public void unadoptTrait(ITraitDimension dimension, ITrait trait);


    /* Tag handling methods */

    public void addTag(IAgentTag tag);

    public void removeTag(IAgentTag tag);

    public Set<IAgentTag> getAgentTags();

    /* Interaction and Rule related methods */

    public void addInteractionRule(InteractionRule rule);

    public void fireRules();

    /* Structure related methods */

    /**
     * Use this to get the list of current ITraits an agent has adopted.
     * Since ITraits count their own frequency, in toto and by tag, this
     * can be useful for certain kinds of frequency-dependent adoption.
     * @return traitList
     */

    public Set<ITrait> getCurrentlyAdoptedTraits();

    public Set<ITrait> getCurrentlyAdoptedTraitsForDimension(ITraitDimension dim);

    public List<IAgent> getNeighboringAgents();



}
