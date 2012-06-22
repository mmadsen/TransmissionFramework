/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import org.madsenlab.sim.tf.interfaces.IStatisticsObserver;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 2:34 PM
 */

public class DiscreteClassesUnitIntervalTraitDimension implements IClassDimension {
    private ITraitDimension<Double> traitDimension;
    private Set<IClassDimensionMode> dimensionModeSet;


    public DiscreteClassesUnitIntervalTraitDimension() {
        this.dimensionModeSet = new HashSet<IClassDimensionMode>();
    }


    @Override
    public void setTraitDimensionToTrack(ITraitDimension dimension) {
        this.traitDimension = dimension;
    }

    @Override
    public Integer getNumDimensionModes() {
        return null;
    }

    /**
     * For the desired number of modes N, creates a random partition of the open unit interval
     * into N complete and non-overlapping intervals, creates a RealTraitIntervalPredicate for each,
     * and creates a DimensionMode with that predicate.
     *
     * @param desiredNumModes
     */

    @Override
    public void createRandomModeSet(Integer desiredNumModes) {

    }

    @Override
    public void createSpecifiedModeSet(Set<TraitPredicate> predicates) {

    }

    @Override
    public Set<IClassDimensionMode> getModeSet() {
        return null;
    }
}
