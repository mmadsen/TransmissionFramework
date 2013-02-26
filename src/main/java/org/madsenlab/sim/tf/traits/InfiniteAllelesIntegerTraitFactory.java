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

public class InfiniteAllelesIntegerTraitFactory implements ITraitFactory {
    Logger log;
    private ISimulationModel model;
    private Integer nextTraitID;
    private Provider<ITrait> traitProvider;


    public InfiniteAllelesIntegerTraitFactory(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
        this.nextTraitID = 0;
        this.traitProvider = this.model.getTraitProvider();
    }

    @Override
    public ITrait getNewUniqueUniformVariant() {
        this.nextTraitID++;
        ITrait newTrait = this.traitProvider.get();
        newTrait.setTraitID(this.nextTraitID);
        return newTrait;
    }

    @Override
    public ITrait getNewVariantBasedUponExistingVariant(ITrait existingTrait) {
        return this.getNewUniqueUniformVariant();
    }

    @Override
    public Boolean providesInfiniteVariants() {
        return Boolean.TRUE;
    }

    @Override
    public Set<ITrait> getUniqueUniformTraitCollection(Integer numTraits) {
        Set<ITrait> traitSet = new HashSet<ITrait>();
        for (int i = 0; i < numTraits; i++) {
            traitSet.add(this.getNewUniqueUniformVariant());
        }
        return traitSet;
    }

    // TODO: unimplemented
    @Override
    public Set<ITrait> getGaussianTraitCollection(Integer numTraits, Double mean, Double stdev) {
        return null;
    }

    // TODO:  unimplemented
    @Override
    public Set<ITrait> getUniformTraitCollection(Integer numTraits) {
        return null;
    }
}
