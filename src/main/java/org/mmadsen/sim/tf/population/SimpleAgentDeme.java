/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IAgentTag;
import org.mmadsen.sim.tf.interfaces.IDeme;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 26, 2010
 * Time: 9:21:47 AM
 */

public class SimpleAgentDeme extends AbstractDeme implements IDeme {

    @Inject
    public void initialize(ISimulationModel m, Provider<IDeme> d) {
        model = m;
        log = model.getModelLogger(this.getClass());
        demeProvider = d;
        
    }

}
