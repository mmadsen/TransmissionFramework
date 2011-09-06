/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import java.io.PrintWriter;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/28/11
 * Time: 2:57 PM
 */

public interface ILogFiles {
    void initializeLogFileHandler();

    PrintWriter getFileWriterForPerRunOutput(String filename);
}
