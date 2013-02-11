/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.apache.commons.collections.Predicate;
import org.madsenlab.sim.tf.interfaces.ITrait;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/20/12
 * Time: 1:26 PM
 */

public abstract class TraitPredicate implements Predicate {
    protected String id;

    public String getID() {
        return this.id;
    }


    public boolean evaluate(Object t) {
        return this.doEvaluate((ITrait) t);
    }

    abstract boolean doEvaluate(ITrait trait);
}
