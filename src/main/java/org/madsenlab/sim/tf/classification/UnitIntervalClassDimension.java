/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 2:34 PM
 */

public class UnitIntervalClassDimension implements IClassDimension {
    private ITraitDimension<Double> traitDimension;
    private Set<IClassDimensionMode> dimensionModeSet;
    private ISimulationModel model;
    private Logger log;


    public UnitIntervalClassDimension(ISimulationModel model, ITraitDimension<Double> traitDimension) {
        this.dimensionModeSet = new HashSet<IClassDimensionMode>();
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
        this.traitDimension = traitDimension;
    }

    @Override
    public Integer getNumDimensionModes() {
        return this.dimensionModeSet.size();
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
        Integer modeNum = 0;

        Set<TraitPredicate> predicateSet = new HashSet<TraitPredicate>();

        List<Double> boundaries = new ArrayList<Double>();
        desiredNumModes--;  // we want k -1 boundaries to produce k classes
        while (desiredNumModes > 0) {
            double rand = this.model.getUniformDouble();
            boundaries.add(rand);
            desiredNumModes--;
        }
        Collections.sort(boundaries);

        double lowerBound = 0.0;
        double upperBound = 0.0;
        for (double bound : boundaries) {
            upperBound = bound;
            TraitPredicate pred = new RealTraitIntervalPredicate(lowerBound, false, upperBound, true, modeNum.toString());
            predicateSet.add(pred);

            log.info("Creating predicate: " + pred.toString());
            lowerBound = upperBound;
            modeNum++;
        }

        upperBound = 1.0;
        TraitPredicate p = new RealTraitIntervalPredicate(lowerBound, false, upperBound, false, modeNum.toString());
        predicateSet.add(p);
        log.info("Creating predicate: " + p.toString());

        // now create actual modes given the set of random predicates.
        this.createSpecifiedModeSet(predicateSet);

    }


    @Override
    public void createSpecifiedModeSet(Set<TraitPredicate> predicates) {
        for (TraitPredicate pred : predicates) {
            IClassDimensionMode mode = new RealIntervalDimensionMode(this.model, this.traitDimension, pred);
            log.debug("Constructing new mode: " + mode + " from predicate: " + pred);
            this.dimensionModeSet.add(mode);
        }
    }

    @Override
    public Set<IClassDimensionMode> getModeSet() {
        return this.dimensionModeSet;
    }
}
