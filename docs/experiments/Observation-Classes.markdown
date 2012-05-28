# Notes on Implementing Observable Classes in TF #

The goal is to effect a split between Traits (the information which flows within the simulated population), and
observational units which are arbitrary with respect to that information, but are used by outside observers to "view"
change in those traits.

Thus, observational units ("Units") are defined by some kind of mapping between a Unit and a set of one or more Traits.
A "classification" would thus consist of a scheme which would map many traits (perhaps belonging to dimensions) to Units.
The important part is that the mapping should be arbitrary for our initial purposes, because we want to see how to
describe the invariant features of a transmission process if we cannot observe its primary information flow.

In real cases, the mapping may not be wholly arbitrary, because we have good reasons for selecting **dimensions** for
specific problems.  Prehistoric artifact designs would produce randomness across our dimensions if our dimension selections
did not related to the same physical constraints and laws that past people also faced.  Thus, while we cannot know
how prehistoric peoples conceived of their design and engineering issues, or their decorative options and "grammars,"
we can capture the shape of the distribution and the amount of variability present, and see how those distributions
map to meaningful distinctions in environment or interaction patterns.  But for purposes of an abstract simulation,
there is no reason not to build arbitrary mappings, and see what happens.

## Requirements ##

1.  Traits are self-counting, and track agent adoption.
1.  Units would be instantiated to map to Traits
1.  This set of Traits would not have to be structured in a particular way.  This would allow the duplication of arbitrary
archaeological classifications within the simulation (i.e., ad-hoc systems) to examine their consequences.
1.  There must, however, be clear support for paradigmatic classification and multi-level classifications.
1.  This points to the need to use UnitDimensions as intersections, to build sets of units.


## Proposed Implementation ##

This is straight from my diss proposal and seems sound as an idea.

1.  New implementation of a Trait:  Each TraitDimension is the unit interval [0,1], and a Trait is a real number from this
interval, held by an Agent.
1.  Infinite-alleles innovation occurs by a new, unused real number being selected within the interval.
1.  This representation allows models of copying error or mutational "distance" as well.
1.  UnitDimensions are orthogonal classificatory structures, but each UnitDimension maps to a TraitDimension, in initial
implementation.
1.  Each UnitDimension has a set of UnitModes, which form a non-overlapping partition of the unit interval [0,1].
1.  Multiple UnitDimensions may point at a TraitDimension.  This allows watching the same simulation model run with multiple
classifications and comparing the resulting statistics.

### Detailed Notes ###

1.  TraitDimension needs a public API for getting the traits in a particular interval of the [0,1] representation.
1.  Most generically, this would be handled in the ITraitDimension API by a TraitPredicate, following the way AgentPredicates
work for selecting subsets of agent lists.
1.  At the moment, ITrait has a getTraitID() method, which returns a string.  Without changing this API, the simplest thing is to
assign the real number a string representation when initializing a trait.  This is sufficient only if nothing needs to be **done**
with the trait representation other than checking uniqueness or copying it, and if all the [0,1] operation s
actually happen in the TraitDimension implementation.
1.  The alternative, which is desirable in the long term, is to define the ITrait API as a generic type marker and allow subclasses to
select the type which will represent the trait through its ID.  In this plan, the TraitIDComparator will definitely need to change.  There
are 44 total uses of getTraitID() which will need to be checked or modified to handle generic types.

