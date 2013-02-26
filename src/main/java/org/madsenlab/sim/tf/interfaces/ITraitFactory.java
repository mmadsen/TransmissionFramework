/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/12/12
 * Time: 2:25 PM
 */

public interface ITraitFactory {

    /**
     * Constructs a new, never before seen ITrait given the underlying model of
     * variation being used, and returns it fully initialized with a unique TraitID
     *
     * @return ITrait
     */

    public ITrait getNewUniqueUniformVariant();

    /**
     * For variation models in which new variants can be created with reference to an existing trait
     * (e.g., perceptual copying error), this constructs a new trait based upon the existing trait
     * passed in.  The implementation of this is up to the specific variation model.
     * <p/>
     * HOWEVER -- in cases
     *
     * @param existingTrait
     * @return
     */

    public ITrait getNewVariantBasedUponExistingVariant(ITrait existingTrait);


    /**
     * returns TRUE if this variation model always provides a new trait, regardless of the
     * underlying representation, or whether an "innovation" can repeat or "back mutate"
     *
     * @return
     */

    public Boolean providesInfiniteVariants();

    /**
     * returns a collection of traits with unique values chosen randomly from the dimension.
     *
     * @return Set<ITrait>
     */

    public Set<ITrait> getUniqueUniformTraitCollection(Integer numTraits);

    /**
     * returns a collection of traits with  values chosen from a Gaussian draw from the dimension.  Traits are
     * NOT guaranteed to be unique.
     *
     * @return Set<ITrait>
     */

    public Set<ITrait> getGaussianTraitCollection(Integer numTraits, Double mean, Double stdev);

    /**
     * returns a collection of traits with potentially non-unique values from the dimension.
     *
     * @return Set<ITrait>
     */

    public Set<ITrait> getUniformTraitCollection(Integer numTraits);
}
