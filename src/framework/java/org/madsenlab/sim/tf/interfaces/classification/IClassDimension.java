/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:42 AM
 */

public interface IClassDimension {

    // TODO:  Add dimension modes
    // TODO:  Add human readable dimension description (for doing simulations on real situations)
    // TODO:  Do we need remove/revise dimension modes?  Maybe second round.

    public Integer getNumDimensionModes();

    /**
     * Creates the desired number of DimensionModes against the tracked TraitDimension.
     * The mode "boundaries" are chosen at random and are not guaranteed to cover
     * equal amounts of the "space" represented by the trait dimension.
     *
     * @param desiredNumModes
     */

    public void createRandomModeSet(Integer desiredNumModes);

    /**
     * Creates DimensionModes against the tracked TraitDimension using the specified
     * set of TraitPredicates. This method is used for replicating specific external
     * classifications and is especially useful for testing.
     *
     * @param predicates
     */

    public void createSpecifiedModeSet(Set<TraitPredicate> predicates);

    /**
     * Returns a set of the DimensionModes for this ClassDimension.  Must be called *after* one of the two
     * create methods are called.
     *
     * @return
     */

    public Set<IClassDimensionMode> getModeSet();


}
