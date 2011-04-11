# Multiple Demes Through Tagging - Experiment #

The goal is to build a simulation of two or more demes or subpopulations, organized into a simple metapopulation model, and see where the current v1.0 API needs adjustment or modification.

## Requirements ##

* NO special-case code is allowed here - this is about finding out where the architecture works, and where it's not finished yet.
* Structure is created only through the use of tags
* Topology employs only tags to implement that structure, through dynamic querying of demes

## Basic Steps ##

1.  Create a copy of SimpleMoranDrift for TwoDemesWithMigration
1.  Interaction is well mixed between demes
1.  Demes are defined by having two tags for agents
1.  InteractionTopology needed which knows the two tags for agents
1.  Do we need an abstraction for topology at agent creation, or is topology simply a tag-based "view" on agents in the population?  Probably the latter if we can make it work.
1.  Need Observers that specifically keep track of frequencies by tag.
1.  Need a migration rule that "moves" a random agent every so often between demes, by switching the tag they have, and a new command line param for the migration rate.
1.  Oh, and add an observer for the number of traits at any given time that are non-zero, or do it in the core frequency reporting observer.

## Open Questions ##

* Do I need an abstraction for a TagSet -- a set of related tags?  Possibly.  Especially if we're using a set of tags for metapopulation structure, possibly a set of tags for social roles, etc.  We might need to enumerate the tags which represent a set of alternatives:  getAgentTagsInSet(IAgentTagSet tagSet)

