/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.apache.commons.collections.Predicate;
import org.madsenlab.sim.tf.interfaces.IArtifact;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 9/5/11
 * Time: 10:21 AM
 */

public abstract class ArtifactPredicate implements Predicate {
    public boolean evaluate(Object a) {
        return this.doEvaluate((IArtifact) a);
    }

    abstract boolean doEvaluate(IArtifact artifact);
}
