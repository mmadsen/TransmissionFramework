/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;
import org.madsenlab.sim.tf.utils.TraitIDComparator;

import java.io.PrintWriter;
import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/12/12
 * Time: 4:28 PM
 */

public class EwensSampleFullPopulationObserver implements IStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private PrintWriter pw;
    private PrintWriter statPW;
    private Map<Integer, Map<ITrait, Integer>> histSamples;
    private Integer sampleSize = 0;
    private List<Integer> numTraitsInSamplePerTick;
    private PrintWriter knPW;


    public EwensSampleFullPopulationObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.histSamples = new HashMap<Integer, Map<ITrait, Integer>>();
        this.numTraitsInSamplePerTick = new ArrayList<Integer>();
        String ewenSampleLogFile = this.model.getModelConfiguration().getProperty("ewens-sample-logfile");
        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(ewenSampleLogFile);
        String ewenStatsLogFile = this.model.getModelConfiguration().getProperty("ewens-kn-summary-statsfile");
        this.statPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(ewenStatsLogFile);
        String knSampleLog = this.model.getModelConfiguration().getProperty("kn-sample-log");
        this.knPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(knSampleLog);
    }

    @Override
    public void setParameterMap(Map<String, String> parameterMap) {

    }

    @Override
    public void updateStatistics(IStatistic<ITraitDimension> stat) {
        log.trace("entering updateStatistics");
        if (this.sampleSize == 0) {
            // memoize this
            this.sampleSize = this.model.getModelConfiguration().getEwensSampleSize();
        }

        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getTimeStartStatistics()) {
            // take a sample from the population, construct a list
            Map<ITrait, Integer> sampleMap = new HashMap<ITrait, Integer>();

            // Need to sample a number of unique agents, and tally their traits.  In other words, we need an agent
            // sample without replacement
            List<IAgent> agentSample = this.model.getPopulation().getRandomAgentSampleWithoutReplacement(this.sampleSize);

            for (IAgent agent : agentSample) {
                ITrait trait = agent.getRandomTraitFromAgent(TraitCopyingMode.CURRENT);
                if (sampleMap.containsKey(trait)) {
                    int cnt = sampleMap.get(trait);
                    cnt++;
                    sampleMap.put(trait, cnt);
                } else {
                    sampleMap.put(trait, 1);
                }
            }

            // add the size of the sample map (k_n for this sample) to a list for descriptive stats.
            this.numTraitsInSamplePerTick.add(sampleMap.size());
            // store the list in histSamples
            this.histSamples.put(this.model.getCurrentModelTime(), sampleMap);
        }
    }

    @Override
    public void perStepAction() {
        log.trace("entering perStepAction");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getTimeStartStatistics()) {
            // occasionally flush the histSamples to log file to prevent memory explosion
            this.printFrequencies();

            Integer tick = this.model.getCurrentModelTime();
            // Every 100 ticks, write historical data to disk and flush it to keep memory usage and performance reasonable.
            if (tick % 100 == 0) {
                this.logFrequencies();
                this.histSamples.clear();
            }
        }
    }

    @Override
    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        // write any remaining histSamples to log file
        this.logFrequencies();
        this.logStats();
        this.logKnSamples();
    }

    @Override
    public void finalizeObservation() {
        this.pw.flush();
        this.pw.close();
        this.statPW.flush();
        this.statPW.close();
        this.knPW.flush();
        this.knPW.close();
    }

    private StringBuffer prepareSlatkinTestInputString(Integer time, Map<ITrait, Integer> sampleMap) {
        StringBuffer sb = new StringBuffer();
        // I really think it's stupid that values() returns something generic so I have to allocate a whole
        // separate object just to sort the damned thing
        Collection<Integer> countSet = sampleMap.values();
        List<Integer> countList = new ArrayList<Integer>(countSet);
        Collections.sort(countList);   // this puts it in ascending order
        Collections.reverse(countList); // this puts it in the descending order req'd by slatkin-enumerate.c
        sb.append(time);
        sb.append("\t");
        for (Integer i : countList) {
            sb.append(i);
            sb.append(" ");
        }
        return sb;
    }

    private StringBuffer prepareSampleLogString(Integer time, Map<ITrait, Integer> sampleMap) {
        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = sampleMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        sb.append(time);
        for (ITrait aTrait : sortedKeys) {
            sb.append(",");
            Integer count = sampleMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(count);
        }
        return sb;
    }

    private void printFrequencies() {
        Integer time = this.model.getCurrentModelTime();

        Map<ITrait, Integer> sampleMap = this.histSamples.get(time);
        //log.debug("printCounts - getting counts for time: " + time + " : " + countMap);

        StringBuffer sb = prepareSlatkinTestInputString(time, sampleMap);
        log.debug(sb.toString());
        sb = null;

    }

    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<Integer> keys = this.histSamples.keySet();
        List<Integer> sortedKeys = new ArrayList<Integer>(keys);
        Collections.sort(sortedKeys);
        for (Integer time : sortedKeys) {
            Map<ITrait, Integer> sampleMap = this.histSamples.get(time);
            StringBuffer sb = prepareSlatkinTestInputString(time, sampleMap);
            sb.append('\n');
            this.pw.write(sb.toString());
            //this.pw.flush();
        }


    }

    private void logStats() {
        log.trace("entering logStats");
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer count : this.numTraitsInSamplePerTick) {
            stats.addValue((double) count);
        }
        this.statPW.write("Samples of size " + this.sampleSize + " taken each tick\n");
        this.statPW.write(stats.toString());
        this.statPW.write("\n");
        this.statPW.write("\n");
        log.info("Ewens K_n Sample Stats: =============");
        log.info("Samples of size " + this.sampleSize + " taken each tick");
        log.info(stats.toString());


    }

    private void logKnSamples() {
        for (Integer count : this.numTraitsInSamplePerTick) {
            this.knPW.write(count.toString());
            this.knPW.write("\n");
        }
    }


}
