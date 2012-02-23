/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/25/11
 * Time: 11:37 AM
 */

public class InfiniteAllelesMutationRule extends AbstractInteractionRule implements IMutationRule {
    Double mutationRate;
    Integer newTraitID;

    public InfiniteAllelesMutationRule(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        this.setRuleName("InfiniteAllelesMutationRule");
        this.setRuleDescription("Randomly mutate trait with a probability to a completely new trait");
        this.mutationRate = this.model.getModelConfiguration().getMutationRate();
        this.newTraitID = 0;
    }

    public void ruleBody(Object o) {
        log.trace("entering rule body for: " + this.getRuleName());
        IAgent thisAgent = (IAgent) o;

        // DECISION TO MUTATE OR COPY IS MADE IN ANOTHER RULE - IF THIS RULE FIRES AT ALL, IT JUST NEEDS
        // TO PERFORM THE RIGHT KIND OF MUTATION

        ITraitDimension dim = this.getRandomTraitDimension();
        log.trace("Number of traits/dim prior to mutation: " + dim.getTraitsInDimension().size());

        log.trace("Selected dimension: " + dim + " for mutation");
        // Generate a new trait
        this.newTraitID++;
        ITrait newTrait = this.model.getNewTrait(dim);
        Integer timeAdded = 100000 + this.newTraitID;
        newTrait.setTraitID(timeAdded.toString());
        dim.addTrait(newTrait);

        ITrait oldTrait = thisAgent.getRandomTraitFromAgent(TraitCopyingMode.CURRENT);
        oldTrait.unadopt(thisAgent);
        newTrait.adopt(thisAgent);

    }

    public void registerSubRule(IActionRule rule) {
        //null in this implemention

    }

    public void deregisterSubRule(IActionRule rule) {
        // null in this implementation

    }

    private ITraitDimension getRandomTraitDimension() {
        List<ITraitDimension> dimensionList = this.model.getTraitDimensions();
        Integer numDimensions = dimensionList.size();
        ITraitDimension dim;

        if(numDimensions == 1) {
            // shortcut the obvious case
            dim = dimensionList.get(0);

        }
        else {
            Integer selectedDimNum = this.model.getUniformRandomInteger(numDimensions - 1);
            dim = dimensionList.get(selectedDimNum);
        }
        return dim;
    }

    @Override
    public void setTraitCopyingMode(TraitCopyingMode mode) {

    }
}
