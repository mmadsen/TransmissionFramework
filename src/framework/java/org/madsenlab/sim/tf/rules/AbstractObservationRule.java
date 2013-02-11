/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.IArtifact;
import org.madsenlab.sim.tf.interfaces.IObservationRule;
import org.madsenlab.sim.tf.interfaces.ITrait;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 9/6/11
 * Time: 4:02 PM
 */

public abstract class AbstractObservationRule extends AbstractActionRule implements IObservationRule {

    public ITrait getRandomTraitFromArtifact(IArtifact thisArtifact) {
        return null;
    }
}
