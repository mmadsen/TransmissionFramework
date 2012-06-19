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
1.  Multiple UnitDimensions may point at a TraitDimension.  This allows watching the same simulation model run with multiple
classifications and comparing the resulting statistics.

## Implementation Ideas ##

1. UnitDimensions point at a single ITraitDimension.
1. Multiple UnitDimensions may point at a single ITraitDimension (allows watching the same simulation run with multiple
classifications and comparing results.

1. UnitDimensions don't have to know anything about underlying trait representations, and thus can be implemented ONCE and given Predicates and other configuration to do their mappings.
1. A UnitMode represents a partition of a UnitDimension in the sense that some subset of ITraits belonging to the underlying ITraitDimension will map to each UnitMode.  UnitModes need not represent actual classes, but may be a concept implemented by mappings, predicates, etc.  
1. A Classification is a collection of UnitDimensions, which can intersect the dimensions and present Classes, each of which is a permutation of the UnitModes from UnitDimensions.  
1. There may need to be additional support in the ITraitDimension extending the trait count methods (but probably not the frequency methods, since frequencies need to be recalculated for Classes anyhow).  Something like getCurGlobalTraitCountsForPredicate(TraitPredicate tp) would allow a UnitDimension to get counts for all traits which currently match the partition for a given UnitMode, and pass them upward.  

## Trouble Ahead... ## 

Crap.  Marginal trait counts for each trait aren't going to help, are they?  Do we need to know the actual individuals themselves, or can we figure out class counts by knowing the marginal counts?  

The solution is perhaps to partially decouple Class counting from Trait Counts.  We need the above description of how to get Classes from UnitDimensions and their class predicates, operating upon the TraitDimensions and variation models.  The end result is that each Class object represents a set of known combinations of traits, or in a continuous model a known chunk of trait space.  

Once the classes are configured before the start of the simulation, the issue then turns to efficiently registering changes in class membership by agents.  We then give  each agent a reference to a Classifier, whose job is evaluating whether an adoption event changes the class(es) to which an agent currently belongs, and if so, we trigger a class class adopt/unadopt pair, as for traits.  

Class counts are Observed just like TraitDimensions, in the modelStep().  

Multiple classifications are accomodated efficiently by having a set of classifications, each associated with a name.  Each agent keeps a hash of <name, class in classification>, and a classifier takes the agent's current class in each classification, and its current trait set, and evaluates class changes for all classifications.  Etc.  









## Dev Notes ##

1.  TraitDimension needs a public API for getting the traits in a particular subset of an underlying representation, without having to hardcode
what that representation is.
1.  Most generically, this would be handled in the ITraitDimension API by a TraitPredicate, following the way AgentPredicates
work for selecting subsets of agent lists, this way you can have TraitPredicates that go with an underlying ITraitFactory and variation model.
1.  The alternative, which is desirable in the long term, is to define the ITrait API as a generic type marker and allow subclasses to
select the type which will represent the trait through its ID.  In this plan, the TraitIDComparator will definitely need to change.  There
are 44 total uses of getTraitID() which will need to be checked or modified to handle generic types.


