/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.AgentPredicate;
import org.madsenlab.sim.tf.utils.AgentTagPredicate;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;

import java.util.*;

/**
 * AbstractDeme provides the base class and most of the implementation for dealing with
 * subpopulations of agents, apart from agent creation and destruction.  Additional methods
 * which should be available on any partition of the population should be added here.
 * <p/>
 * Subclasses should provide a means of initializing the population and ensuring valid references
 * to the ISimulationModel which implements a given agent-based simulation.  If a subclass provides
 * only initialization services, and only implements IDeme, the resulting IDeme object will be
 * functionally read-only.
 * <p/>
 * If a subclass provides agent creation/destruction methods (e.g., by implementing IPopulation),
 * the resulting object will be mutable and capable of full population dynamics.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * User: mark
 * Date: Sep 30, 2010
 * Time: 2:17:12 PM
 */

public class AbstractDeme implements IDeme {
    protected ISimulationModel model;
    protected Logger log;
    protected List<IAgent> agentList;
    @Inject
    protected Provider<IDeme> demeProvider;

    public void setAgentList(List<IAgent> agents) {
        this.agentList = agents;
    }

    public List<IAgent> getAgents() {
        return new ArrayList<IAgent>(agentList);
    }

    public IAgent getAgentAtRandom() {
        Integer randomAgentNumber = this.model.getUniformRandomInteger(this.getCurrentPopulationSize() - 1);
        return this.agentList.get(randomAgentNumber);
    }

    public List<IAgent> getAgentsShuffledOrder() {
        List<IAgent> shuffledAgents = new ArrayList<IAgent>(agentList);
        Collections.shuffle(shuffledAgents);
        return shuffledAgents;
    }

    public IDeme getDemeForTag(IAgentTag tag) {
        AgentPredicate pred = new AgentTagPredicate(tag);
        return this.getDemeMatchingPredicate(pred);
    }

    public IDeme getDemeMatchingPredicate(AgentPredicate pred) {
        //log.trace("demeProvider: " + demeProvider);
        List<IAgent> matches = new ArrayList<IAgent>();

        for (IAgent agent : agentList) {
            if (pred.evaluate(agent)) {
                matches.add(agent);
                //log.info("adding agent to match list: " + agent);
            }
        }

        //log.trace("size of match list: " + matches.size());

        IDeme subDeme = demeProvider.get();
        subDeme.setAgentList(matches);

        return subDeme;
    }

    public Integer getCurrentPopulationSize() {
        //log.trace("agentList size: " + this.agentList.size());
        return this.agentList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Boolean hasMemberAgents() {
        if (getCurrentPopulationSize() == 0) {
            return false;
        } else return true;
    }

    @Override
    public List<IAgent> getRandomAgentSampleWithoutReplacement(Integer size) {
        int uniqueAgents = 0;
        List<IAgent> agentSample = new ArrayList<IAgent>();
        while (uniqueAgents != size) {
            IAgent agent = this.getAgentAtRandom();
            if (!agentSample.contains(agent)) {
                agentSample.add(agent);
                uniqueAgents++;
            }
        }
        //log.debug("agentSample: " + agentSample);
        return agentSample;
    }

    public ITrait getMostFrequentTrait(TraitCopyingMode mode) {
        SortedSet<Map.Entry<ITrait, Integer>> sortedTraits = this.getSortedTraitCountsForAgentList(this.agentList, mode);
        Map.Entry<ITrait, Integer> highestEntry = sortedTraits.last();
        log.trace("highest trait: " + highestEntry.getKey().getTraitID() + " count: " + highestEntry.getValue());
        return highestEntry.getKey();
    }

    public ITrait getLeastFrequentTrait(TraitCopyingMode mode) {
        SortedSet<Map.Entry<ITrait, Integer>> sortedTraits = this.getSortedTraitCountsForAgentList(this.agentList, mode);
        Map.Entry<ITrait, Integer> lowestEntry = sortedTraits.first();
        log.trace("lowest trait: " + lowestEntry.getKey().getTraitID() + " count: " + lowestEntry.getValue());
        return lowestEntry.getKey();
    }


    private SortedSet<Map.Entry<ITrait, Integer>> getSortedTraitCountsForAgentList(List<IAgent> agentList, TraitCopyingMode mode) {
        Map<ITrait, Integer> countMap = new HashMap<ITrait, Integer>();
        Set<ITrait> myTraits;
        for (IAgent agent : agentList) {
            if (mode == TraitCopyingMode.CURRENT) {
                myTraits = agent.getCurrentlyAdoptedTraits();
            } else {
                myTraits = agent.getPreviousStepAdoptedTraits();
            }

            for (ITrait trait : myTraits) {
                if (countMap.containsKey(trait)) {
                    int cnt = countMap.get(trait);
                    cnt++;
                    countMap.put(trait, cnt);
                } else {
                    countMap.put(trait, 1);
                }
            }
        }
        return entriesSortedByValues(countMap);
    }

    static SortedSet<Map.Entry<ITrait, Integer>> entriesSortedByValues(Map<ITrait, Integer> map) {
        SortedSet<Map.Entry<ITrait, Integer>> sortedEntries = new TreeSet<Map.Entry<ITrait, Integer>>(
                new Comparator<Map.Entry<ITrait, Integer>>() {
                    @Override
                    public int compare(Map.Entry<ITrait, Integer> e1, Map.Entry<ITrait, Integer> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    /*static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }*/
}
