/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.  
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.agent;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.enums.TraitCopyingMode;

import java.util.ArrayList;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/13/12
 * Time: 2:06 PM
 */

public abstract class AbstractAgent implements IAgent {
    protected Logger log;
    protected ISimulationModel model;

    public ITrait getRandomTraitFromAgent(TraitCopyingMode mode) {
        Set<ITrait> focalTraits;


        if (mode == TraitCopyingMode.CURRENT) {
            focalTraits = this.getCurrentlyAdoptedTraits();
        } else {
            focalTraits = this.getPreviousStepAdoptedTraits();
        }
        ITrait focalTrait;

        if (focalTraits.size() == 1) {
            focalTrait = focalTraits.iterator().next();
        } else {
            log.trace("focal agent has more than one trait, selecting random trait");
            ArrayList<ITrait> focalTraitList = new ArrayList<ITrait>(focalTraits);
            int numTraits = focalTraitList.size();
            int randomTrait = this.model.getUniformRandomInteger(numTraits - 1);
            focalTrait = focalTraitList.get(randomTrait);
        }
        return focalTrait;
    }
}
