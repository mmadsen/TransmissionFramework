# Notes on Observers and Classification #


## Current Implementation of Trait Observers #

### Outline of Main Method Call Loop ###

Current implementation of observers is that ITraitDimensions have an observer list, and once per model step, the ISimulationModel object calls notifyObservers() on the dimension object(s).  

notifyObservers() then requests a IStatistic object from the dimension itself, and for each observer in the observerList, calls updateStatistics() with the IStatistic as an argument.  Currently, the IStatistic is sort of a misnomer, since all it houses is a pointer to the dimension that changed, and a time index for the observation, not an indication of specifically what changed, nor any "statistics" about the change.  

At the end of a model step, the ISimulationModel then calls modelObservations(), which goes through all observer lists, and calls perStepAction() on each observer.  Typically, this method does things like debug printing of trait frequencies, and flushing counts or frequencies to disk or databases.  

At the end of a simulation run, the ISimulationModel object then runs modelFinalize(), which calls endSimulationAction() on all observers.  This method is typically used to calculate any final statistical summaries, and flush remaining raw data and any statistical summaries to disk or database.  

### Details of Trait Counting ###

Within updateStatistics(), the trait count observer (e.g., GlobalTraitCountObserver) first check to see if the model time is beyond the "start time" for gathering statistics.  If it is, then the observer uses the IStatistic object to retrieve the target ITraitDimension and model time index for the observation.  The dimension, when asked for current trait counts, simply iterates and asks for the current adoption count.  

This structure is repeated, with predicate filtering, for getting counts by tag.  Similarly, retrieving frequencies rather than counts works the same way.  

## Implementation Notes for Classification Counting ##

__Notes:__

1.  Each classification should keep its own class counts.  Main model objects (e.g., Agents, TraitDimensions) should not know anything about classes or class memberships.  
2.  Class adoption/unadoption requires us to identify whether changes in traits for an agent result in changes to class membership, given significata.  
3.  We then do a fairly standard observer pattern on the classifications to run through class counts each model step.  

This implies that the class identification interface (regardless of what implements it) should support being handed an IPopulation object (which supersets IDeme), and should memoize the current class identification for each agent at the end of an identification step, so that it can be used in the next time step for class change detection.  

In this implementation, the model object would first call notify() on all trait observers.  And then iterate over all classifications, performing class identification, and finally calling notify() on all classification observers.  


Remember….ISimulationModels can be configured to have more than one IClassification, pointing to the same set of trait dimensions.  


### Class Identification ###

__Main task:__

* Begin with an IAgent, who has one or more ITraits, each belonging to an ITraitDimension.  
* Assume the context of one IClassification.
* First identify the proper IClassDimension for each ITraitDimension involved.
* Ask the IClassDimension for the proper IClassDimensionMode for a given ITrait value.
* Repeat for each ITraitDimension
* Given a set of IClassDimensionModes, identify the proper IClass.

__Identify IClass given modes:__

Inside the classification, we need to index the IClasses by mode, for fast lookup.  Need to research this a bit, but the fastest implementation might be to generate a compound key made of the concatenation of all mode names, instead of doing some kind of complicated set intersection thing.  We only have to pregenerate an index once….


 


