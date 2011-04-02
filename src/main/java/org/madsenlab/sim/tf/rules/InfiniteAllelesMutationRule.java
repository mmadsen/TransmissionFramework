/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.rules;

import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/25/11
 * Time: 11:37 AM
 */

public class InfiniteAllelesMutationRule extends AbstractInteractionRule {
    Double mutationRate;

    public InfiniteAllelesMutationRule(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        this.setRuleName("InfiniteAllelesMutationRule");
        this.setRuleDescription("Randomly mutate trait with a probability to a completely new trait");
        this.mutationRate = this.model.getModelConfiguration().getMutationRate();
    }

    public void ruleBody(Object o) {
        log.trace("entering rule body for: " + this.getRuleName());
        IAgent thisAgent = (IAgent) o;


        // Generate a random double between 0 and 1, if this value is less than the mutation rate,
        // a mutation "event" has occurred.  If not, the rule body does nothing.
        Double chance = this.model.getUniformDouble();
        if( chance < this.mutationRate ) {
            ITraitDimension dim = this.getRandomTraitDimension();
            log.trace("Number of traits/dim prior to mutation: " + dim.getTraitsInDimension().size());

            log.trace("Mutation occurred - chance : " + chance + " < mutationRate: " + this.mutationRate);
            log.debug("Selected dimension: " + dim + " for mutation");
            // Generate a new trait
            ITrait newTrait = this.model.getNewTrait(dim);
            Integer timeAdded = 100000 + this.model.getCurrentModelTime();
            newTrait.setTraitID(timeAdded.toString());
            dim.addTrait(newTrait);

            ITrait oldTrait = this.getRandomTraitFromAgent(thisAgent);
            oldTrait.unadopt(thisAgent);
            newTrait.adopt(thisAgent);


        }

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

}
