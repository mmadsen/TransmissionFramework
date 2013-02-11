/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import org.apache.commons.collections.Closure;

/**
 * A type of agent rule that governs the observation of artifacts by agents, in the same way that
 * an IInteractionRule governs the social interaction and copying between agents.
 * <p/>
 * <p/>
 * User: mark
 * Date: 9/5/11
 * Time: 1:55 PM
 */

public interface IObservationRule extends Closure, IActionRule {

    ITrait getRandomTraitFromArtifact(IArtifact thisArtifact);
}
