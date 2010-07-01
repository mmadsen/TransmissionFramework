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
