/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.agent;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.*;

/**
 * UnstructuredTraitDimensions represent a set of alternative traits
 * which are normally mutually exclusive
 *
 * @author Mark E. Madsen
 */
public class UnstructuredTraitAgent implements IAgent {
    private String agentID;
    private ISimulationModel model;
    private Logger log;
    private Set<ITrait> traitsAdopted;
    private Set<IAgentTag> tagSet;
    private List<IInteractionRule> ruleList;

    public UnstructuredTraitAgent() {
        super();
        initialize();
    }

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
    }

    private void initialize() {
        this.traitsAdopted = Collections.synchronizedSet(new HashSet<ITrait>());
        this.tagSet = Collections.synchronizedSet(new HashSet<IAgentTag>());
        this.ruleList = new ArrayList<IInteractionRule>();
    }

    public String getAgentID() {
        return this.agentID;
    }

    public void setAgentID(String id) {
        this.agentID = id;
    }

    public void adoptTrait(ITrait trait) {
        synchronized (this.traitsAdopted) {
            this.traitsAdopted.add(trait);
        }
    }

    public void adoptTrait(ITraitDimension dimension, ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unadoptTrait(ITrait trait) {
        synchronized (this.traitsAdopted) {
            this.traitsAdopted.remove(trait);
        }

    }

    public void unadoptTrait(ITraitDimension dimension, ITrait trait) {

    }

    public void addTag(IAgentTag tag) {

        synchronized (this.tagSet) {
            this.tagSet.add(tag);
        }

    }

    public void removeTag(IAgentTag tag) {

        synchronized (this.tagSet) {
            this.tagSet.remove(tag);
        }
    }

    /**
     * Returns the current set of IAgentTags to which this agent belongs.  If there are
     * no tags in use in a particular simulation model, this method is guaranteed to return
     * non-null
     *
     * @return
     */

    public Set<IAgentTag> getAgentTags() {
        return new HashSet<IAgentTag>(this.tagSet);
    }

    public boolean hasTag(IAgentTag tag) {
        boolean containsTag = this.tagSet.contains(tag);
        //log.info("hasTag returned: " + containsTag);
        return containsTag;
    }

    public void addInteractionRule(IInteractionRule rule) {
        this.ruleList.add(rule);
    }

    public void addInteractionRuleList(List<IInteractionRule> fullRuleList) {
        this.ruleList = null;
        this.ruleList = fullRuleList;
    }


    /*
        Because the Apache Commons functor library doesn't support generic types, IInteractionRules
        take Objects, but really what needs to be passed in are IAgent objects.  Rules already
        have access to the Model object by virtue of their construction and initialization
        which means they have access to the IPopulation and so on.  What they need when executed
        is the context of a single agent, in order that the rule can change agent state.

     */
    public void fireRules() {

        for(IInteractionRule rule: ruleList) {
            rule.execute(this);
        }
    }

    public Set<ITrait> getCurrentlyAdoptedTraits() {
        return new HashSet<ITrait>(this.traitsAdopted);
    }

    public Set<ITrait> getCurrentlyAdoptedTraitsForDimension(ITraitDimension dim) {
        return null;
    }

    public List<IAgent> getNeighboringAgents() {
        return null;
    }

    public void addTraitDimension(ITraitDimension dimension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
