/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import com.google.inject.Inject;
import org.madsenlab.sim.tf.interfaces.ILogFiles;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

import java.io.*;
import java.util.UUID;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/28/11
 * Time: 1:55 PM
 */


public class LogFileHandler implements ILogFiles {
    @Inject
    private ISimulationModel model;
    private File mainOutputDirectory;
    private String stringOutputDirectory;
    private String uniqueRunIdentifier;


    public LogFileHandler() {

    }

    /**
     * Cannot use log4j yet in this code, since it will be called by the method which initializes log4j
     */
    public void initializeLogFileHandler(String loggingRootDirectory) {

        this.createUniquePerRunIdentifier();

        String outputParentDirectory = loggingRootDirectory;
        StringBuffer sb = new StringBuffer();
        sb.append(outputParentDirectory);
        sb.append("/");
        sb.append(this.uniqueRunIdentifier);

        this.stringOutputDirectory = sb.toString();


        try {
            this.mainOutputDirectory = new File(sb.toString());
            this.mainOutputDirectory.mkdir();
        } catch (SecurityException ex) {
            System.out.println("exception creating log file directory: " + ex.getMessage());
            System.exit(1);
        }

    }

    public PrintWriter getFileWriterForPerRunOutput(String filename) {
        File outputFile = new File(this.mainOutputDirectory, filename);
        PrintWriter outFileWriter = null;
        try {
            outFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputFile), 32768));
        } catch (IOException ioe) {
            System.out.println("IOException on filepath: " + outputFile.toString() + ": " + ioe.getMessage());
            System.exit(1);
        }
        return outFileWriter;
    }

    public String getLoggingDirectory() {
        return this.stringOutputDirectory;
    }

    private void createUniquePerRunIdentifier() {
        // Generate a unique run identifier

        UUID uniqueID = UUID.randomUUID();
        StringBuffer sb = new StringBuffer();
        sb.append("tf");
        sb.append("-");
        sb.append(uniqueID.toString());

        this.uniqueRunIdentifier = sb.toString();

    }


}
