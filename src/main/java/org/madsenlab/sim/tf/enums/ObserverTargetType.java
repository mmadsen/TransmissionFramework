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
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/8/12
 * Time: 3:01 PM
 */
public enum ObserverTargetType {
    TRAITDIMENSION("TRAITDIMENSION"),
    CLASSIFICATION("CLASSIFICATION");


    private final String targetType;

    private static final Map<String, ObserverTargetType> stringToEnum = new ConcurrentHashMap<String, ObserverTargetType>();

    static {
        for (ObserverTargetType set : values()) {
            stringToEnum.put(set.targetType, set);
        }
    }

    private ObserverTargetType(String setting) {
        this.targetType = setting;
    }

    public String toString() {
        return this.targetType;
    }

    public static ObserverTargetType fromString(String setting) {
        return stringToEnum.get(setting);
    }
}
