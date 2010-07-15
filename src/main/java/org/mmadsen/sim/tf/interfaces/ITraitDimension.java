/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.interfaces;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    public void addTrait(ITrait newTrait);

    public ITrait getTrait(String traitID);

    public Collection<ITrait> getTraitsInDimension();

    public Map<String,Integer> getCurGlobalTraitCounts();

    public Map<String,Double> getCurGlobalTraitFrequencies();

    //public void removeTrait(ITrait traitToRemove);
}
