/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.structure;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IArtifact;
import org.madsenlab.sim.tf.interfaces.IArtifactTag;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.enums.ArtifactTagType;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 9/9/11
 * Time: 12:43 PM
 */

public class SimpleArtifactTag implements IArtifactTag {
    private ISimulationModel model;
    private Logger log;
    private String tagname;
    private Integer curArtifactCount;
    private List<IArtifact> artifactList;
    private Map<Integer, Integer> histArtifactCountMap;
    private ArtifactTagType type;

    public SimpleArtifactTag() {
        this.initialize();
    }

    @Inject
    public void setSimulationModel(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
    }

    private void initialize() {
        this.curArtifactCount = 0;
        this.artifactList = Collections.synchronizedList(new ArrayList<IArtifact>());
        this.histArtifactCountMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    }

    public String getTagName() {
        return this.tagname;
    }

    public void setTagName(String tagname) {
        this.tagname = tagname;
    }

    public List<IArtifact> getCurArtifactsTagged() {
        return new ArrayList<IArtifact>(this.artifactList);
    }

    public void registerArtifact(IArtifact artifact) {
        Preconditions.checkNotNull(artifact);
        this.incrementAdoptionCount();
        log.trace("Artifact registering:" + artifact.getArtifactID());
        synchronized (this.artifactList) {
            if (this.artifactList.contains(artifact) == false) {
                this.artifactList.add(artifact);
                artifact.addTag(this);
            }
        }
    }

    public void unregisterArtifact(IArtifact artifact) {
        Preconditions.checkNotNull(artifact);
        this.decrementAdoptionCount();
        log.trace("Artifact unregistering: " + artifact.getArtifactID());
        synchronized (this.artifactList) {
            this.artifactList.remove(artifact);
            artifact.removeTag(this);
        }
    }

    public Integer curArtifactCount() {
        return this.curArtifactCount;
    }

    public Map<Integer, Integer> getArtifactCountHistory() {
        return new HashMap<Integer, Integer>(this.histArtifactCountMap);
    }

    public void setTagType(ArtifactTagType type) {
        this.type = type;
    }

    public ArtifactTagType getTagType() {
        return this.type;
    }

    private synchronized void incrementAdoptionCount() {
        this.curArtifactCount++;
        this.histArtifactCountMap.put(this.model.getCurrentModelTime(), this.curArtifactCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curArtifactCount--;
        this.histArtifactCountMap.put(this.model.getCurrentModelTime(), this.curArtifactCount);
    }
}
