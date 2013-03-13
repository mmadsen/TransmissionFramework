/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.interfaces.*;

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

    public String getClassificationName();

    public void setClassificationName(String name);

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
    public IClassIdentificationEngine getClassIdentifier();

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
     * For each class dimension in the classification (each of which points at a trait dimension),
     * we need to create a set of modes which will form the classes through which we observe
     * change in the underlying trait space.  This method assumes that a set of class dimensions
     * have been added, and creates a random set of modes for each dimension, in preparation for
     * intersection into IClass objects.
     *
     */

    //public void createRandomModesForDimensions();


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

    public Map<Integer, IClass> getTableClassIDandClass();

    public Map<Integer, String> getTableClassIDandDescription();

    /*  Statistical Methods */

    public Map<IClass, Integer> getCurGlobalClassCounts();

    public Map<IClass, Integer> getCurClassCountByTag(IAgentTag tag);

    /*
        Methods for calculating frequencies will throw an IllegalStateException if the size of the
        population against which the calculation depends is zero.  This alerts calling code to the
        fact that something is being done out of order, not initialized, etc.
     */

    public Map<IClass, Double> getCurGlobalClassFrequencies();

    public Map<IClass, Double> getCurClassFreqByTag(IAgentTag tag);

    public IClass getClassForTraits(Map<ITraitDimension, ITrait> traitMap);

    /**
     * Given previous and currently adopted traits for a particular agent, determines if an agent has
     * "changed class" given its traits, and if so, it does the class unadopt/adopt pair to update class
     * counts.
     * <p/>
     * This method is one of the main usages of this interface, and should be used to update system state after every
     * trait adopt event.
     */

    public void updateClassForAgent(IAgent agent);

    /**
     * Given an IPopulation object, determines for each agent if a class has changed, and if so, executes
     * the appropriate class unadopt/adopt pair to update class adoption counts.
     * <p/>
     * This method is one of the main usages of this interface, and for many types of models will be the method
     * used after each model step but before notifying classification observers to record class counts.
     */

    public void updateClassForPopulation(IPopulation pop);


}
