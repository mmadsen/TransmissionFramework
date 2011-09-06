/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import org.madsenlab.sim.tf.utils.ArtifactPredicate;

import java.util.List;
import java.util.Set;

/**
 * Container for populations of artifacts -- essentially a material culture counterpart to a deme/population.
 *
 * <p/>
 * User: mark
 * Date: 9/5/11
 * Time: 10:50 AM
 */

public interface IArtifactCollection {

    public IArtifact createArtifact();

    public IArtifact createArtifactWithTraits(Set<ITrait> traitSet);

    /**
     * Returns a typed List of artifacts currently in the population.  This list is a shallow
     * clone of the internal list of artifacts, so modifying the returned list does not affect
     * the underlying set of artifacts.
     *
     * @return
     */
    public List<IArtifact> getArtifacts();

    /**
     * Returns one artifact from the given collection, chosen at random from a uniform
     * distribution.
     *
     * @return artifact
     */
    IArtifact getArtifactAtRandom();

    /**
     * Query interface, returning an IArtifactCollection instance which is populated by the artifacts for which
     * a Predicate (whether simple or compound/chained) returns TRUE.
     *
     * @param pred
     * @return subpopulation
     */
    IDeme getArtifactCollectionMatchingPredicate(ArtifactPredicate pred);

    /**
     * Returns the current size of the artifact population for a given collection.
     *
     * @return popSize The number of individual artifacts in the collection at the current time
     */

    Integer getCurrentArtifactCount();

    /**
     * Simple method to indicate whether a query resulting in a collection actually had any results,
     * so that we don't have issues with trying to do processing on empty lists.
     *
     * @return
     */
    Boolean hasArtifacts();

}
