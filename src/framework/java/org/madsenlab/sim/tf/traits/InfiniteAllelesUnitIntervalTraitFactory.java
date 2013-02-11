/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/12/12
 * Time: 3:00 PM
 */

public class InfiniteAllelesUnitIntervalTraitFactory implements ITraitFactory {
    Logger log;
    private ISimulationModel model;
    private Provider<ITrait> traitProvider;
    private Set<Double> existingTraitSet;


    public InfiniteAllelesUnitIntervalTraitFactory(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
        this.traitProvider = this.model.getTraitProvider();
        this.existingTraitSet = new HashSet<Double>();
    }

    @Override
    public ITrait getNewVariant() {
        Double nextTraitID = null;
        Boolean foundUniqueVariant = false;
        while (foundUniqueVariant == false) {
            nextTraitID = this.model.getUniformDouble();
            if (this.existingTraitSet.contains(nextTraitID) == false) {
                foundUniqueVariant = true;
            }
        }

        // nextTraitID is now guaranteed to be a double we haven't seen before, to the limits of precision.
        // keep track of it so we don't pick it again.
        this.existingTraitSet.add(nextTraitID);
        ITrait newTrait = this.traitProvider.get();
        newTrait.setTraitID(nextTraitID);
        return newTrait;
    }

    @Override
    public ITrait getNewVariantBasedUponExistingVariant(ITrait existingTrait) {
        return this.getNewVariant();
    }

    @Override
    public Boolean providesInfiniteVariants() {
        return Boolean.TRUE;
    }
}
