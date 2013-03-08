/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import com.google.common.base.Preconditions;
import org.madsenlab.sim.tf.utils.ClassDimensionModeType;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;

import java.util.HashSet;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/6/13
 * Time: 12:23 PM
 */

public class ClassificationDimensionConfiguration {
    private Integer traitDimensionTracked;
    private ClassDimensionModeType modeType;
    private Set<RealTraitIntervalPredicate> modePredicates;
    private Integer numberModesForRandomModeType;

    public ClassDimensionModeType getModeType() {
        return modeType;
    }

    public Integer getNumberModesForRandomModeType() {
        return numberModesForRandomModeType;
    }

    public void setNumberModesForRandomModeType(Integer numberModesForRandomModeType) {
        this.numberModesForRandomModeType = numberModesForRandomModeType;
    }

    public ClassificationDimensionConfiguration() {

    }

    public Integer getTraitDimensionTracked() {
        return traitDimensionTracked;
    }

    public void setTraitDimensionTracked(Integer traitDimensionTracked) {
        this.traitDimensionTracked = traitDimensionTracked;
    }


    public void setModeType(String modeTypeString) {
        this.modeType = ClassDimensionModeType.fromString(modeTypeString);
        if (this.modeType.equals(ClassDimensionModeType.SPECIFIED)) {
            this.modePredicates = new HashSet<RealTraitIntervalPredicate>();
        }
    }

    public void addModePredicate(RealTraitIntervalPredicate pred) {
        Preconditions.checkNotNull(this.modePredicates, "You called addModePredicate without first calling setModeType with type SPECIFIED");

        this.modePredicates.add(pred);
    }

    public Set<RealTraitIntervalPredicate> getModePredicates() {
        return this.modePredicates;
    }

}
