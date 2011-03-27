/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.apache.commons.collections.Predicate;
import org.madsenlab.sim.tf.interfaces.IAgent;

/**
 * Abstract base class for predicates which operate on an Agent
 * This would be pretty unnecessary if the Apache Commons predicate
 * implementation were generic....
 * <p/>
 * User: mark
 * Date: Aug 26, 2010
 * Time: 11:15:10 AM
 */

public abstract class AgentPredicate implements Predicate {
    public boolean evaluate(Object o) {
        return this.doEvaluate((IAgent) o);
    }

    abstract boolean doEvaluate(IAgent agent);
}
