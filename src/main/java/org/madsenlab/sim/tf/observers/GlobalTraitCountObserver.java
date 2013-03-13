/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import hep.aida.ref.Converter;
import hep.aida.ref.Histogram1D;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.ObserverConfiguration;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.Log2HistogramBinUtil;
import org.madsenlab.sim.tf.utils.TraitIDComparator;

import java.io.PrintWriter;
import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 3:19:51 PM
 */


public class GlobalTraitCountObserver implements IStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private Map<ITrait, Integer> traitCountMap;
    private Integer lastTimeIndexUpdated;
    private PrintWriter pw;
    private Map<Integer, Map<ITrait, Integer>> histTraitCount;
    private List<Integer> totalTraitsPerTick;
    private PrintWriter statsPW;
    private Histogram1D histo;
    private ObserverConfiguration config;

    public GlobalTraitCountObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.histTraitCount = new HashMap<Integer, Map<ITrait, Integer>>();
        this.totalTraitsPerTick = new ArrayList<Integer>();

        this.config = this.model.getModelConfiguration().getObserverConfigurationForClass(this.getClass());

        String traitFreqLogFile = this.config.getParameter("global-trait-count-logfile");
        String countStatsLogFile = this.config.getParameter("global-trait-count-statsfile");

        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(traitFreqLogFile);
        this.statsPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(countStatsLogFile);

        // initialize a histogram for counts with bins in powers of two
        Log2HistogramBinUtil histoBinUtil = new Log2HistogramBinUtil(this.model);
        double[] bins = histoBinUtil.getLog2HistogramBins();
        this.histo = new Histogram1D("Trait Count Histogram", bins);

    }

    @Override
    public void setParameterMap(Map<String, String> parameterMap) {

    }

    // we only want to start recording trait counts after the initial transient behavior decays and we reach equilibrium
    public void updateStatistics(IStatistic<ITraitDimension> stat) {
        log.trace("entering updateStatistics");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {
            this.lastTimeIndexUpdated = stat.getTimeIndex();
            ITraitDimension dim = stat.getTarget();
            Map<ITrait, Integer> countMap = dim.getCurGlobalTraitCounts();
            this.histTraitCount.put(this.lastTimeIndexUpdated, countMap);
            this.totalTraitsPerTick.add(countMap.keySet().size());
        }
    }

    // we only want to start recording trait counts after the initial transient behavior decays and we reach equilibrium
    public void perStepAction() {
        log.trace("entering perStepAction");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {
            //log.trace("histTraitFreq: " + this.histTraitFreq);
            this.printFrequencies();

            if (this.model.getCurrentModelTime() == this.model.getModelConfiguration().getSimlength() - 1) {
                Map<ITrait, Integer> countMap = this.histTraitCount.get(this.model.getCurrentModelTime());
                for (ITrait trait : countMap.keySet()) {
                    this.histo.fill((double) countMap.get(trait));
                }
                Converter conv = new Converter();
                log.info(conv.toString(this.histo));
            }

            Integer tick = this.model.getCurrentModelTime();
            // Every 100 ticks, write historical data to disk and flush it to keep memory usage and performance reasonable.
            if (tick % 100 == 0) {
                this.logFrequencies();
                this.histTraitCount.clear();
            }
        }
    }

    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        // this is only required if the simulation length is not evenly divisible by 100
        this.logFrequencies();

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer val : this.totalTraitsPerTick) {
            stats.addValue((double) val);
        }
        log.info("Trait Count Stats: =============");
        log.info(stats.toString());

        this.statsPW.write(stats.toString());
        this.logFrequencies();
    }

    public void finalizeObservation() {
        log.trace("entering finalizeObservation");
        this.pw.flush();
        this.pw.close();
        this.statsPW.flush();
        this.statsPW.close();
    }

    private void printFrequencies() {
        Integer time = this.model.getCurrentModelTime();

        Map<ITrait, Integer> countMap = this.histTraitCount.get(time);
        //log.debug("printCounts - getting counts for time: " + time + " : " + countMap);

        StringBuffer sb = prepareCountLogString(time, countMap);
        log.debug(sb.toString());
        sb = null;

    }

    private StringBuffer prepareCountLogString(Integer time, Map<ITrait, Integer> countMap) {
        //log.debug("prepare string: freqMap: " + freqMap);
        Integer numNonZeroTraits = 0;
        Integer totalOfTraitCounts = 0;
        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = countMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        sb.append(time);
        sb.append(",");
        for (ITrait aTrait : sortedKeys) {
            Integer count = countMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(count);
            sb.append(",");
            if (count != 0) {
                numNonZeroTraits++;
            }
            totalOfTraitCounts += count;
        }
        sb.append("tot:");
        sb.append(totalOfTraitCounts);
        sb.append(",numtrait:");
        sb.append(numNonZeroTraits);
        return sb;
    }


    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<Integer> keys = this.histTraitCount.keySet();
        List<Integer> sortedKeys = new ArrayList<Integer>(keys);
        Collections.sort(sortedKeys);
        for (Integer time : sortedKeys) {
            Map<ITrait, Integer> countMap = this.histTraitCount.get(time);
            StringBuffer sb = prepareCountLogString(time, countMap);
            sb.append('\n');
            this.pw.write(sb.toString());
            //this.pw.flush();
        }

    }
}
