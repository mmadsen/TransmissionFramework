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

    public String getDimensionName();

    public void addTrait(ITrait newTrait);

    public ITrait getTrait(String traitID);

    public Collection<ITrait> getTraitsInDimension();

    public Map<String,Integer> getCurGlobalTraitCounts();

    public Map<String,Double> getCurGlobalTraitFrequencies();

    //public void removeTrait(ITrait traitToRemove);
}
