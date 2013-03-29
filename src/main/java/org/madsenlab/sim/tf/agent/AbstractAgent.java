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
import org.madsenlab.sim.tf.enums.TraitCopyingMode;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;

import java.util.*;

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
    protected Integer numTraitDimensionsExpected = 0;
    protected Boolean agentInitialized = Boolean.FALSE;         // data structures for current traits
    protected Set<ITraitDimension> dimensionSet;
    protected Set<ITrait> traitSet;
    protected Map<ITrait, ITraitDimension> traitToDimensionMap;
    protected Map<ITraitDimension, ITrait> dimensionToTraitMap;
    protected Map<ITrait, ITraitDimension> traitToDimensionMapLastStep;
    protected Map<ITraitDimension, ITrait> dimensionToTraitMapLastStep;
    protected Set<ITrait> traitsLastStep;
    protected Set<ITraitDimension> dimensionsLastStep;

    @Override
    public void setAgentInitialized(Boolean state) {
        this.agentInitialized = state;
    }

    @Override
    public void setNumTraitDimensionsExpected(Integer numTraitDimensionsExpected) {
        this.numTraitDimensionsExpected = numTraitDimensionsExpected;
    }

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

    @Override
    public ITrait getTraitFromDimensionFromAgent(TraitCopyingMode mode, ITraitDimension dim) {
        ITrait trait = null;
        if (mode == TraitCopyingMode.CURRENT) {
            trait = this.getCurrentlyAdoptedTraitForDimension(dim);
        } else {
            trait = this.getPreviouslyAdoptedTraitForDimension(dim);
        }
        log.debug("dimension: " + dim.toString() + " trait: " + trait.toString());
        return trait;
    }

    @Override
    public ITraitDimension getRandomTraitDimensionFromAgent() {
        List<ITraitDimension> dimensionList = new ArrayList<ITraitDimension>(this.dimensionSet);
        Collections.shuffle(dimensionList);
        return dimensionList.get(0);
    }

}
