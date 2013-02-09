/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
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
 * An ITraitDimension is analogous to a "locus" which can have many variant alleles, or a haplotype.
 * Each dimension can be represented by a finite or infinite number of ITraits, which can have differing
 * underlying representations (i.e., discrete categories perhaps represented by integer tags, continuous
 * variables represented by a float, or arbitrarily structured traits which might be "recipes").
 * <p/>
 * Each ITraitDimension has a "variation model," by which new traits are generated (i.e., by innovation
 * or mutation), and is the object through which trait counts and especially frequencies are obtained
 * (although ITraits are responsible for "counting themselves" upon adoption or unadoption events).
 */
public interface ITraitDimension<T> extends IStatisticsSubject, ITraitFactory {

    public void setSimulationModel(ISimulationModel m);

    public void setTraitVariationModel(ITraitFactory f);

    public String getDimensionName();

    public void setDimensionName(String name);

    // deprecated now that we're using the ITraitFactory, this migrated
    //public void addTrait(ITrait<T> newTrait);

    /**
     * Returns a new ITrait with the specified value.  This cannot be included in the ITraitFactory
     * interface because that interface is not a generic and does not expose the underlying type.  So
     * we do it here.  Often this will not be used in models, but in validation and verification tests.
     *
     * @param T value
     * @return
     */

    public ITrait<T> getNewVariantWithSpecifiedValue(T val);

    public ITrait<T> getTrait(T traitID);

    public Collection<ITrait<T>> getTraitsInDimension();

    public ITrait getRandomTraitFromDimension();

    public Map<ITrait<T>, Integer> getCurGlobalTraitCounts();

    public Map<ITrait<T>, Integer> getCurTraitCountByTag(IAgentTag tag);

    /*
        Methods for calculating frequencies will throw an IllegalStateException if the size of the
        population against which the calculation depends is zero.  This alerts calling code to the
        fact that something is being done out of order, not initialized, etc.
     */

    public Map<ITrait<T>, Double> getCurGlobalTraitFrequencies();

    public Map<ITrait<T>, Double> getCurTraitFreqByTag(IAgentTag tag);

    //public void removeTrait(ITrait<T> traitToRemove);
}
