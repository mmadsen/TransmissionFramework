/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/6/13
 * Time: 11:29 AM
 */
public enum ClassDimensionModeType {
    SPECIFIED("SPECIFIED"),
    RANDOM("RANDOM");

    private final String modeType;

    private static final Map<String, ClassDimensionModeType> stringToEnum = new ConcurrentHashMap<String, ClassDimensionModeType>();

    static {
        for (ClassDimensionModeType set : values()) {
            stringToEnum.put(set.modeType, set);
        }
    }

    private ClassDimensionModeType(String setting) {
        this.modeType = setting;
    }

    public String toString() {
        return this.modeType;
    }

    public static ClassDimensionModeType fromString(String setting) {
        return stringToEnum.get(setting);
    }
}
