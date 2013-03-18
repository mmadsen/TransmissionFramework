/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Some classes may need to perform calculations differently, depending upon
 * the way "time" runs in a model.  Time, in a model, is a relationship between
 * an elemental number of copying events, and the advancement of the model's
 * clock.  In a Wright-Fisher style model, for example, time advances after N
 * copying events, so we think of this as a "discrete" model with
 * non-overlapping generations.  Moran models, in contrast, advance time with
 * every elemental copying event, so they represent a close approximation to
 * a continuous-time process.
 * <p/>
 * Within TransmissionFramework, CONTINUOUS should be interpreted as time
 * running in units of 1 copying event (or at rate 1/N), and DISCRETE as time
 * running in units of N copying events (or at rate 1).
 * <p/>
 * <p/>
 * User: mark
 * Date: 2/14/12
 * Time: 1:58 PM
 */
public enum GenerationDynamicsMode {
    CONTINUOUS("CONTINUOUS"),
    DISCRETE("DISCRETE");

    private final String modeType;

    private static final Map<String, GenerationDynamicsMode> stringToEnum = new ConcurrentHashMap<String, GenerationDynamicsMode>();

    static {
        for (GenerationDynamicsMode set : values()) {
            stringToEnum.put(set.modeType, set);
        }
    }

    private GenerationDynamicsMode(String setting) {
        this.modeType = setting;
    }

    public String toString() {
        return this.modeType;
    }

    public static GenerationDynamicsMode fromString(String setting) {
        return stringToEnum.get(setting);
    }
}
