/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.agent;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 11, 2010
 * Time: 12:33:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleAgent implements IAgent {
    private String agentID;
    private ISimulationModel model;
    private Logger log;
    private List<ITrait> traitsAdopted;
    private List<IAgentTag> tagList;

    public SimpleAgent() {
        super();
        initialize();
    }

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        log.debug("setSimulationModel called and agent object initialized");
    }

    private void initialize() {
        this.traitsAdopted = Collections.synchronizedList(new ArrayList<ITrait>());
        this.tagList = Collections.synchronizedList(new ArrayList<IAgentTag>());
    }

    public String getAgentID() {
        return this.agentID;
    }

    public void setAgentID(String id) {
        this.agentID = id;
    }

    public void adoptTrait(ITrait trait) {
        synchronized(this.traitsAdopted) {
            this.traitsAdopted.add(trait);
        }
    }

    public void adoptTrait(ITraitDimension dimension, ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unadoptTrait(ITrait trait) {
        synchronized(this.traitsAdopted) {
            this.traitsAdopted.remove(trait);
        }

    }

    public void unadoptTrait(ITraitDimension dimension, ITrait trait) {

    }

    public void addTag(IAgentTag tag) {
        synchronized(this.tagList) {
            this.tagList.add(tag);
        }

    }

    public void removeTag(IAgentTag tag) {
        synchronized(this.tagList) {
            this.tagList.remove(tag);
        }
    }

    /**
     * Returns the current set of IAgentTags to which this agent belongs.  If there are
     * no tags in use in a particular simulation model, this method is guaranteed to return
     * non-null
     * @return
     */
    
    public List<IAgentTag> getAgentTags() {
        return new ArrayList<IAgentTag>(this.tagList);
    }

    public List<ITrait> getCurrentlyAdoptedTraits() {
        return new ArrayList<ITrait>(this.traitsAdopted);
    }

    public List<IAgent> getNeighboringAgents() {
        return null;
    }

    public void addTraitDimension(ITraitDimension dimension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
