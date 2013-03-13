/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import org.madsenlab.sim.tf.utils.GenerationDynamicsMode;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/27/13
 * Time: 7:58 AM
 */

public interface IModelDynamics {
    public void modelStep();

    public GenerationDynamicsMode getGenerationDynamicsMode();
}
