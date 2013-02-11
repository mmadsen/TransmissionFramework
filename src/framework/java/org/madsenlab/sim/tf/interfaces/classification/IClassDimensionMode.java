/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:43 AM
 */

public interface IClassDimensionMode {

    /**
     * Returns the trait predicate used to define a given mode
     *
     * @return
     */
    public TraitPredicate getPredicateDefiningMode();

    /**
     * Returns a string description of the mode, given its underlying predicate
     *
     * @return
     */
    public String getModeDescription();

    /**
     * Returns the current set of traits which are mapped to this mode. Given the argument, either
     * returns just the currently adopted set of traits (i.e., those with non-zero adoption counts),
     * or the entire historical set of traits which have mapped to this mode, to date (i.e., including
     * those with zero adoption counts).
     * <p/>
     * This is NOT an indication of what the mode "boundaries" are, given a specific variation model.
     *
     * @return
     */
    public Set<ITrait> getTraitsMappedToMode(Boolean includeTraitsWithZeroAdoptionCount);


}
