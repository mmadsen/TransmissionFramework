/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IInteractionRule;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;

import java.util.ArrayList;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 25, 2010
 * Time: 4:20:49 PM
 */

public abstract class AbstractInteractionRule extends AbstractActionRule implements IInteractionRule {

    // if anything except a rule uses this code, might want to refactor it into IAgent and impl. classes
    public ITrait getRandomTraitFromAgent(IAgent thisAgent, TraitCopyingMode mode) {
        Set<ITrait> focalTraits;

        if(mode == TraitCopyingMode.CURRENT) {
            focalTraits = thisAgent.getCurrentlyAdoptedTraits();
        } else  {
            focalTraits = thisAgent.getPreviousStepAdoptedTraits();
        }
        ITrait focalTrait;

        if(focalTraits.size() == 1) {
            focalTrait = focalTraits.iterator().next();
        }
        else {
            log.trace("focal agent has more than one trait, selecting random trait");
            ArrayList<ITrait> focalTraitList = new ArrayList<ITrait>(focalTraits);
            int numTraits = focalTraitList.size();
            int randomTrait = this.model.getUniformRandomInteger(numTraits - 1);
            focalTrait = focalTraitList.get(randomTrait);
        }
        return focalTrait;
    }


}
