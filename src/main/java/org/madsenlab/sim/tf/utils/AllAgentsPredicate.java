/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import com.google.common.base.Preconditions;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IAgentTag;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 12/11/10
 * Time: 3:49 PM
 */

public class AllAgentsPredicate extends AgentPredicate {

    boolean doEvaluate(IAgent agent) {
        return true;
    }
}


