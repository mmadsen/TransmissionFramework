/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.agent;

import com.google.inject.Inject;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.AgentTagType;

import java.util.*;

/**
 * UnstructuredMultidimensionalTraitAgent
 * <p/>
 * <p/>
 * <p/>
 * User: mark
 * Date: Jul 23, 2010
 * Time: 3:26:08 PM
 */


public class UnstructuredMultidimensionalTraitAgent extends AbstractAgent {
    private String agentID;
    private Set<IAgentTag> tagSet;

    // data structures for current traits
    private Set<ITraitDimension> dimensionSet;
    private Set<ITrait> traitSet;
    private Map<ITrait, ITraitDimension> traitToDimensionMap;
    private Map<ITraitDimension, ITrait> dimensionToTraitMap;

    // data structures for traits from previous step
    private Set<ITrait> traitsLastStep;
    private Set<ITraitDimension> dimensionsLastStep;
    private Map<ITrait, ITraitDimension> traitToDimensionMapLastStep;
    private Map<ITraitDimension, ITrait> dimensionToTraitMapLastStep;


    public UnstructuredMultidimensionalTraitAgent() {
        super();
        initialize();
    }

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
        initialize();
    }

    private void initialize() {
        this.dimensionSet = Collections.synchronizedSet(new HashSet<ITraitDimension>());
        this.traitSet = Collections.synchronizedSet(new HashSet<ITrait>());
        this.traitToDimensionMap = Collections.synchronizedMap(new HashMap<ITrait, ITraitDimension>());
        this.dimensionToTraitMap = Collections.synchronizedMap(new HashMap<ITraitDimension, ITrait>());
        this.tagSet = Collections.synchronizedSet(new HashSet<IAgentTag>());

    }

    public String getAgentID() {
        return this.agentID;
    }

    public void setAgentID(String id) {
        this.agentID = id;
    }

    public void adoptTrait(ITrait trait) {
        ITraitDimension dim = trait.getOwningDimension();
        _doAdoptTrait(dim, trait);
    }

    public void adoptTrait(ITraitDimension dimension, ITrait trait) {
        _doAdoptTrait(dimension, trait);
    }

    private void _doAdoptTrait(ITraitDimension dim, ITrait trait) {
        synchronized (this.traitSet) {
            this.traitSet.add(trait);
        }
        synchronized (this.traitToDimensionMap) {
            this.traitToDimensionMap.put(trait, dim);
        }
        synchronized (this.dimensionToTraitMap) {
            this.dimensionToTraitMap.put(dim, trait);
        }
    }


    public void unadoptTrait(ITrait trait) {
        ITraitDimension dim = trait.getOwningDimension();
        _doUnadoptTrait(dim, trait);
    }

    public void unadoptTrait(ITraitDimension dim, ITrait trait) {
        _doUnadoptTrait(dim, trait);
    }

    private void _doUnadoptTrait(ITraitDimension dim, ITrait trait) {
        synchronized (this.traitSet) {
            this.traitSet.remove(trait);
        }
        synchronized (this.traitToDimensionMap) {
            this.traitToDimensionMap.remove(trait);
        }
        synchronized (this.dimensionToTraitMap) {
            this.dimensionToTraitMap.remove(dim);
        }
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

    public Set<IAgentTag> getAgentTags() {
        return new HashSet<IAgentTag>(this.tagSet);
    }

    public Set<IAgentTag> getAgentTagsMatchingType(AgentTagType type) {
        return null;
    }

    public boolean hasTag(IAgentTag tag) {
        return this.tagSet.contains(tag);
    }

    public void addActionRule(IActionRule rule) {

    }

    public void addActionRuleList(List<IActionRule> ruleList) {

    }

    public void fireRules() {

    }

    @Override
    public Map<ITraitDimension, ITrait> getCurrentlyAdoptedDimensionsAndTraits() {
        return new HashMap<ITraitDimension, ITrait>(this.dimensionToTraitMap);
    }

    @Override
    public Map<ITraitDimension, ITrait> getPreviousStepAdoptedDimensionsAndTraits() {
        return new HashMap<ITraitDimension, ITrait>(this.dimensionToTraitMapLastStep);
    }

    public Set<ITrait> getCurrentlyAdoptedTraits() {
        return new HashSet<ITrait>(this.traitSet);
    }

    public ITrait getCurrentlyAdoptedTraitForDimension(ITraitDimension dim) {
        return this.dimensionToTraitMap.get(dim);
    }

    @Override
    public void savePreviousStepTraits() {
        this.traitsLastStep = null;
        this.dimensionsLastStep = null;
        this.traitToDimensionMapLastStep = null;
        this.dimensionToTraitMapLastStep = null;

        this.traitsLastStep = Collections.synchronizedSet(new HashSet<ITrait>(this.traitSet));
        this.dimensionsLastStep = Collections.synchronizedSet(new HashSet<ITraitDimension>(this.dimensionSet));
        this.dimensionToTraitMapLastStep = Collections.synchronizedMap(new HashMap<ITraitDimension, ITrait>());
        this.traitToDimensionMapLastStep = Collections.synchronizedMap(new HashMap<ITrait, ITraitDimension>());
    }

    @Override
    public Set<ITrait> getPreviousStepAdoptedTraits() {
        return this.traitsLastStep;
    }

    public List<IAgent> getNeighboringAgents() {
        return null;
    }
}