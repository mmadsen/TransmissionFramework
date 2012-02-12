/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.analysis;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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


public class GlobalTraitLifetimeObserver implements ITraitStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private Map<ITrait, Integer> traitLifetimeMap;
    private Integer lastTimeIndexUpdated;
    private PrintWriter pw;

    public GlobalTraitLifetimeObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.traitLifetimeMap = new HashMap<ITrait, Integer>();

        String traitFreqLogFile = this.model.getModelConfiguration().getProperty("global-trait-lifetimes");
        log.debug("traitCountLogFile: " + traitFreqLogFile);
        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(traitFreqLogFile);
//        try{
//            this.pw = new PrintWriter(new BufferedWriter(new FileWriter("trait-frequencies-by-time.txt")));
//        } catch(IOException ex) {
//            log.error("ERROR CREATING TRAIT FREQUENCIES BY TIME LOG FILE");
//            System.exit(1);
//        }
    }

    public void updateTraitStatistics(ITraitStatistic<ITraitDimension> stat) {
        log.trace("entering updateTraitStatistics");
        this.lastTimeIndexUpdated = stat.getTimeIndex();
        ITraitDimension dim = stat.getTarget();
        // TODO - basically we look at traits, and put any that have finite positive lifetimes in our stash
        Collection<ITrait> traitCollection = dim.getTraitsInDimension();
        for (ITrait trait : traitCollection) {
            if (trait.hasCompleteDuration()) {
                this.traitLifetimeMap.put(trait, trait.getTraitDuration());
            }
        }
    }

    public void perStepAction() {
        log.trace("entering perStepAction");
        // TODO normally we don't do anything here but for debugging, do some output
        this.printFrequencies();

    }

    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        // TODO - this is where the action is.  Calculate stats, output stats, and then a vector of raw data
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer val : this.traitLifetimeMap.values()) {
            stats.addValue((double) val);
        }
        log.info("Trait Lifetime Stats: =============");
        log.info(stats.toString());

        this.pw.write(stats.toString());
        this.pw.write(" ");
        this.logFrequencies();
    }

    public void finalizeObservation() {
        log.trace("entering finalizeObservation");
        this.pw.flush();
        this.pw.close();
    }

    private void printFrequencies() {

        if (log.getLevel() == Level.DEBUG) {
            Integer time = this.model.getCurrentModelTime();
            StringBuffer sb = prepareLogString(time);
            log.trace(sb.toString());
        }
    }

    private StringBuffer prepareLogString(Integer time) {
        //log.debug("prepare string: freqMap: " + freqMap);

        StringBuffer sb = new StringBuffer();

        Set<ITrait> keys = this.traitLifetimeMap.keySet();
        List<ITrait> sortedTraits = new ArrayList<ITrait>(keys);
        Collections.sort(sortedTraits, new TraitIDComparator());
        sb.append(time);
        for (ITrait aTrait : sortedTraits) {
            sb.append(",");
            Integer lifetime = this.traitLifetimeMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(lifetime);
        }

        return sb;
    }


    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<ITrait> keys = this.traitLifetimeMap.keySet();
        List<ITrait> sortedTraits = new ArrayList<ITrait>(keys);
        Collections.sort(sortedTraits, new TraitIDComparator());
        for (ITrait trait : sortedTraits) {
            StringBuffer sb = new StringBuffer();
            sb.append(trait.getTraitID());
            sb.append("\t");
            sb.append(this.traitLifetimeMap.get(trait));
            sb.append("\n");
            this.pw.write(sb.toString());
        }
        this.pw.flush();
    }
}
