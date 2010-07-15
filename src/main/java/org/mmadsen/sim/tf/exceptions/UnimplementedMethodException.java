/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 5:10:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnimplementedMethodException extends RuntimeException {
      public UnimplementedMethodException(Class clazz, String methodname) {
           super("Method " + methodname + " of class " + clazz.getSimpleName() + " not implemented");
      }
}
