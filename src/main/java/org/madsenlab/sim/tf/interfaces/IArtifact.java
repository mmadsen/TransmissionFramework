/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import org.madsenlab.sim.tf.enums.ArtifactTagType;

import java.util.Set;

/**
 * Interface for artifacts and material culture within TF.
 * <p/>
 * User: mark
 * Date: 9/5/11
 * Time: 10:02 AM
 */

public interface IArtifact {

    public void setSimulationModel(ISimulationModel m);

    public String getArtifactID();

    public void setArtifactID(String id);

    /* Trait related methods -- artifacts are simply created with a set of traits */

    // This should call an analog to adoption on each trait in the set, so the trait
    // can track what artifacts currently exist for that trait.  Traits are not "extinct"
    // unless (1) no agent has adopted the trait, AND, (2) no artifacts exist which contain
    // the trait for observation

    public void setTraits(Set<ITrait> traitSet);



     /* Tag handling methods */

    public void addTag(IArtifactTag tag);

    public void removeTag(IArtifactTag tag);

    public Set<IArtifactTag> getArtifactTags();

    public Set<IArtifactTag> getArtifactTagsMatchingType(ArtifactTagType type);

    public boolean hasTag(IArtifactTag tag);

}
