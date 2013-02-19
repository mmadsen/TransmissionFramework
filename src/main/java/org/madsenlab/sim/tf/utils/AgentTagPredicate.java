/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
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
 * Date: Sep 26, 2010
 * Time: 8:50:09 AM
 */

public class AgentTagPredicate extends AgentPredicate {
    IAgentTag tag;

    public AgentTagPredicate(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Constructing an AgentTagPredicate requires a non-null reference to an IAgentTag object");
        this.tag = tag;
    }

    boolean doEvaluate(IAgent agent) {
        return agent.hasTag(this.tag);
    }
}
