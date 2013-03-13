/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.ObserverConfiguration;
import org.madsenlab.sim.tf.interfaces.*;
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


public class GlobalTraitFrequencyObserver implements IStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private Map<ITrait, Double> traitFreqMap;
    private Integer lastTimeIndexUpdated;
    private PrintWriter pw;
    private Map<Integer, Map<ITrait, Double>> histTraitFreq;
    private ObserverConfiguration config;

    public GlobalTraitFrequencyObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.histTraitFreq = new HashMap<Integer, Map<ITrait, Double>>();

        this.config = this.model.getModelConfiguration().getObserverConfigurationForClass(this.getClass());

        String traitFreqLogFile = this.config.getParameter("global-trait-frequency-logfile");
        log.debug("traitFreqLogFile: " + traitFreqLogFile);
        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(traitFreqLogFile);
    }


    @Override
    public void setParameterMap(Map<String, String> parameterMap) {

    }

    public void updateStatistics(IStatistic<ITraitDimension> stat) {
        log.trace("entering updateStatistics");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {
            this.lastTimeIndexUpdated = stat.getTimeIndex();
            ITraitDimension dim = stat.getTarget();
            Map<ITrait, Double> freqMap = dim.getCurGlobalTraitFrequencies();
            this.histTraitFreq.put(this.lastTimeIndexUpdated, freqMap);
        }
    }

    public void perStepAction() {
        log.trace("entering perStepAction");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {

            //log.trace("histTraitFreq: " + this.histTraitFreq);
            this.printFrequencies();

            Integer tick = this.model.getCurrentModelTime();
            // Every 100 ticks, write historical data to disk and flush it to keep memory usage and performance reasonable.
            if (tick % 100 == 0) {
                this.logFrequencies();
                this.histTraitFreq.clear();
            }
        }

    }

    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        this.logFrequencies();
    }

    public void finalizeObservation() {
        log.trace("entering finalizeObservation");

        this.pw.flush();
        this.pw.close();


    }

    private void printFrequencies() {
        Integer time = this.model.getCurrentModelTime();

        Map<ITrait, Double> freqMap = this.histTraitFreq.get(time);
        //log.debug("printFrequencies - getting freqs for time: " + time + " : " + freqMap);

        StringBuffer sb = prepareFrequencyLogString(time, freqMap);
        log.debug(sb.toString());
        sb = null;

    }

    private StringBuffer prepareFrequencyLogString(Integer time, Map<ITrait, Double> freqMap) {
        //log.debug("prepare string: freqMap: " + freqMap);
        Integer numNonZeroTraits = 0;
        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = freqMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        sb.append(time);
        sb.append(",");
        for (ITrait aTrait : sortedKeys) {
            double freq = freqMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(freq);
            sb.append(",");
            if (freq != 0) {
                numNonZeroTraits++;
            }
        }
        sb.append(numNonZeroTraits);
        return sb;
    }

    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<Integer> keys = this.histTraitFreq.keySet();
        List<Integer> sortedKeys = new ArrayList<Integer>(keys);
        Collections.sort(sortedKeys);
        for (Integer time : sortedKeys) {
            Map<ITrait, Double> freqMap = this.histTraitFreq.get(time);
            StringBuffer sb = prepareFrequencyLogString(time, freqMap);
            sb.append('\n');
            this.pw.write(sb.toString());
        }

    }
}
