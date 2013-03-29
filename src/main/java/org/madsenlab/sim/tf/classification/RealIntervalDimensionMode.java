/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;
import org.madsenlab.sim.tf.utils.NonZeroAdoptionCountTraitPredicate;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 5:58 PM
 */

public class RealIntervalDimensionMode implements IClassDimensionMode {
    private ITraitDimension<Double> watchedDimension;
    private ISimulationModel model;
    private Logger log;
    private TraitPredicate modePredicate;
    private IClassDimension owningClassDimension;

    public RealIntervalDimensionMode(ISimulationModel model, IClassDimension classDimension, ITraitDimension<Double> watched, TraitPredicate modePredicate) {
        this.model = model;
        this.modePredicate = modePredicate;
        this.watchedDimension = watched;
        this.owningClassDimension = classDimension;
        this.log = this.model.getModelLogger(this.getClass());
    }


    @Override
    public TraitPredicate getPredicateDefiningMode() {
        return this.modePredicate;
    }

    @Override
    public String getModeDescription() {
        return this.modePredicate.toString();
    }

    public int compareTo(IClassDimensionMode mode) {
        return this.getModeDescription().compareTo(mode.getModeDescription());
    }

    @Override
    public Set<ITrait> getTraitsMappedToMode(Boolean includeTraitsWithZeroAdoptionCount) {
        log.debug("entering getTraitsMappedToMode");
        Set<ITrait> resultSet = new HashSet<ITrait>();
        Collection<ITrait<Double>> traits = this.watchedDimension.getTraitsInDimension();

        log.trace("traits before predicate filter: " + traits);

        Collection<ITrait<Double>> mappedTraits = CollectionUtils.select(traits, this.modePredicate);

        log.debug("mappedTraits: " + mappedTraits);

        if (includeTraitsWithZeroAdoptionCount == Boolean.FALSE) {
            mappedTraits = CollectionUtils.select(mappedTraits, new NonZeroAdoptionCountTraitPredicate());
        }


        if (mappedTraits.size() > 0) {
            log.debug("mappedTraits size: " + mappedTraits.size());
            resultSet.addAll(mappedTraits);
        }

        log.debug("mode: num traits in result set " + resultSet.size());
        log.debug("exiting getTraitsMappedToMode");
        return resultSet;
    }

    @Override
    public IClassDimension getOwningClassDimension() {
        return this.owningClassDimension;
    }


}
