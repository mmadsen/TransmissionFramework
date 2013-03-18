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
public enum TraitCopyingMode {
    CURRENT("CURRENT"),
    PREVIOUS("PREVIOUS");


    private final String modeType;

    private static final Map<String, TraitCopyingMode> stringToEnum = new ConcurrentHashMap<String, TraitCopyingMode>();

    static {
        for (TraitCopyingMode set : values()) {
            stringToEnum.put(set.modeType, set);
        }
    }

    private TraitCopyingMode(String setting) {
        this.modeType = setting;
    }

    public String toString() {
        return this.modeType;
    }

    public static TraitCopyingMode fromString(String setting) {
        return stringToEnum.get(setting);
    }
}
