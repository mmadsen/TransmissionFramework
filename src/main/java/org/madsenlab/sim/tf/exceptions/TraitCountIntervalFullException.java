/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.exceptions;

/**
 * Exception class thrown if one tries to ask for a trait duration while the trait still has a non-zero adoption count.
 * This extends a runtime exception because we *want* to stop a model programmer in their tracks if they're not properly
 * guarding against this condition, since it will drastically screw up statistical estimates of trait lifetime otherwise.
 * <p/>
 * User: mark
 * Date: 2/11/12
 * Time: 12:05 PM
 */

public class TraitCountIntervalFullException extends RuntimeException {
    public TraitCountIntervalFullException(Class clazz, String methodname) {
        super("Method " + methodname + " of class " + clazz.getSimpleName() + " not implemented");
    }
}
