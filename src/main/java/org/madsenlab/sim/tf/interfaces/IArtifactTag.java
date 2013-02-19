/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import org.madsenlab.sim.tf.utils.ArtifactTagType;

import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 9/5/11
 * Time: 10:24 AM
 */

public interface IArtifactTag {

    public void setSimulationModel(ISimulationModel m);

    public String getTagName();

    public void setTagName(String tagname);


    public List<IArtifact> getCurArtifactsTagged();

    public void registerArtifact(IArtifact artifact);

    public void unregisterArtifact(IArtifact artifact);


    public Integer curArtifactCount();

    public Map<Integer, Integer> getArtifactCountHistory();

    public void setTagType(ArtifactTagType type);

    public ArtifactTagType getTagType();

}