/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.madsenlab.sim.tf.interfaces.ITrait;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/24/12
 * Time: 3:58 PM
 */

public class NonZeroAdoptionCountTraitPredicate extends TraitPredicate {

    @Override
    boolean doEvaluate(ITrait trait) {
        Integer count = trait.getCurrentAdoptionCount();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
