/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 27, 2010
 * Time: 11:26:16 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ITraitDimension {

    public void setSimulationModel(ISimulationModel m);

    public String getDimensionName();

    public void setDimensionName(String name);

    public void addTrait(ITrait newTrait);

    public ITrait getTrait(String traitID);

    public Collection<ITrait> getTraitsInDimension();

    public ITrait getRandomTraitFromDimension();

    public Map<ITrait, Integer> getCurGlobalTraitCounts();

    public Map<ITrait, Integer> getCurTraitCountByTag(IAgentTag tag);

    /*
        Methods for calculating frequencies will throw an IllegalStateException if the size of the
        population against which the calculation depends is zero.  This alerts calling code to the
        fact that something is being done out of order, not initialized, etc.
     */

    public Map<ITrait, Double> getCurGlobalTraitFrequencies();

    public Map<ITrait, Double> getCurTraitFreqByTag(IAgentTag tag);

    public void removeTrait(ITrait traitToRemove);
}
