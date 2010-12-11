/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.interfaces;

import com.google.common.base.Predicate;
import org.apache.commons.collections.Closure;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 24, 2010
 * Time: 3:26:37 PM
 */

public interface IInteractionRule extends Closure {

    public void ruleBody(Object o);

}
