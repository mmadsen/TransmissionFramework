/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.IStatisticsSubject;

import java.util.Map;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:44 AM
 */

public interface IClassification extends IStatisticsSubject {

    public Set<IClassDimension> getClassificationDimensions();

    public Integer getNumClasses();

    /**
     * Return an object capable of identifying agents or sets of traits as to class membership
     * for this classification.  Each classification has its own identifier.  This may or may not
     * be a separate Java class; if not, this method simply returns the classification object itself
     * if it implements the identifier interface.
     *
     * @return
     */
    public IClassIdentifier getClassIdentifier();

    /**
     * To form a classification, we first create IClassDimensions which point at underlying
     * TraitDimensions.  Then, we add each IClassDimension to the classification.
     * <p/>
     * After all dimensions have been called, the classes are created by intersection.
     *
     * @param dim
     * @link initializeClassesByIntersection
     */

    public void addClassDimension(IClassDimension dim);


    /**
     * Called after a set of ClassDimensions has been added, this method
     * intersects all ClassDimensionModes belonging to all the dimensions, and creates an
     * IClass for each mode intersection.
     * <p/>
     * This method must be called before the classification is used to "observe" an
     * underlying set of traits.
     */
    public void initializeClasses();

    /**
     * Returns the set of classes created by intersection.
     *
     * @return
     */
    public Set<IClass> getClasses();

    public Map<IClass, Integer> getCurGlobalClassCounts();

    public Map<IClass, Integer> getCurClassCountByTag(IAgentTag tag);

    /*
        Methods for calculating frequencies will throw an IllegalStateException if the size of the
        population against which the calculation depends is zero.  This alerts calling code to the
        fact that something is being done out of order, not initialized, etc.
     */

    public Map<IClass, Double> getCurGlobalClassFrequencies();

    public Map<IClass, Double> getCurClassFreqByTag(IAgentTag tag);


}
