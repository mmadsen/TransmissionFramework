package org.mmadsen.sim.tf.interfaces;

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

    public void addTrait(ITrait newTrait);

    public ITrait getTrait(String traitID);

    public Set<ITrait> getTraitsInDimension();

    public Map<String,Integer> getCurrentTraitCountMap();

    public Map<String,Double> getCurrentTraitFreqMap();

    public void removeTrait(ITrait traitToRemove);
}
