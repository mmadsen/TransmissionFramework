/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.GlobalModelConfiguration;
import org.madsenlab.sim.tf.interfaces.ILogFiles;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

import java.io.*;
import java.util.Date;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/28/11
 * Time: 1:55 PM
 */

// TODO:  Switch the observer classes to use the ILogFiles handler to actually construct their log files


public class LogFileHandler implements ILogFiles {
    @Inject
    private ISimulationModel model;
    private Logger log;
    private File mainOutputDirectory;
    private String uniqueRunIdentifier;
    private GlobalModelConfiguration params;


    public LogFileHandler() {
        this.log = Logger.getLogger(this.getClass());
    }

    public void initializeLogFileHandler() {
        this.params = this.model.getModelConfiguration();
        this.createUniquePerRunIdentifier();

        String outputParentDirectory = this.params.getProperty("log-parent-directory");
        StringBuffer sb = new StringBuffer();
        sb.append(outputParentDirectory);
        sb.append("/");
        sb.append(this.uniqueRunIdentifier);

        String logDirectory = sb.toString();
        log.info("log file directory for this run: " + logDirectory);

        try {
            this.mainOutputDirectory = new File(sb.toString());
            this.mainOutputDirectory.mkdir();
        }
        catch(SecurityException ex) {
            log.error("FATAL EXCEPTION: " + ex.getMessage());
            System.exit(1);
        }

    }

    public PrintWriter getFileWriterForPerRunOutput(String filename) {
        File outputFile = new File(this.mainOutputDirectory, filename);
        PrintWriter outFileWriter = null;
        try {
            outFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        }
        catch (IOException ioe) {
			log.error("IOException on filepath: " + outputFile.toString() + ": " + ioe.getMessage());
            System.exit(1);
		}
        return outFileWriter;
    }

    private void createUniquePerRunIdentifier() {
        // Generate a unique run identifier
        Date now = new Date();
        Integer maxTraits = this.params.getMaxTraits();
        String maxTraitsString;
        if(maxTraits == Integer.MAX_VALUE) {
            maxTraitsString = new String("INF");
        }
        else {
            maxTraitsString = maxTraits.toString();
        }
        StringBuffer ident = new StringBuffer();
        String batchPrefix = this.params.getProperty("batch-identifier-prefix");
        ident.append(batchPrefix);
        ident.append("-");
        ident.append(this.params.getNumAgents());
        ident.append("-");
        ident.append(this.params.getMutationRate());
        ident.append("-");
        ident.append(this.params.getStartingTraits());
        ident.append("-");
        ident.append(maxTraitsString);
        ident.append("-");
        ident.append(now.getTime());

        this.uniqueRunIdentifier = ident.toString();
    }




}
