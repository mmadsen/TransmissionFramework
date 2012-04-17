# Contact #

Please send all queries related to TransmissionFramework to:  transmissionframework@box.squadmail.com

This will ensure that questions are properly distributed to those working on the system, including its original author.  


# Introduction #

TransmissionFramework (TF) is a Java framework and library for constructing numerical simulation models of cultural transmission and social learning phenomena.

TF is the next generation of my TransmissionLab system, which had some design limitations which prevented the easy construction of models with individual-level heterogeneity in transmission rules. TransmissionLab also relied upon RepastJ, an agent-based simulation framework. TF does not, and is intended to be used in a variety of simulation contexts. TF is also intended to be thread-safe, so it can be used to write concurrent simulations (although this is not a primary goal).

# Major Design Goals #

* Minimize the amount of "tracking" and "accounting" work a simulation author needs to do. Make the "traits" (defined quite broadly) that agents adopt and which flow through the population self-counting under all circumstances.

* Allow simulation authors to easily set up multiple mesoscopic or macroscopic "observables" in their simulations, without having to understand the internals of the framework. Simulation authors should simply be able to register one of their objects as an Observer for a given observable, and the framework should provide a steady stream of frequency or occurrence counts.  The Observer is then responsible for calculating whatever macroscopic quantity needed from the stream of data.

* Avoid pre-defining the notion of "mesoscopic" structure, and allow this to be defined as much as possible by simulation authors. Clearly, spatial and network structure requires specific support in the framework code, and this is provided. But in addition, the framework should support arbitrary structure through the use of "tagging," both for agents and for traits. Tags serve as arbitrary partitions of the agent and trait universes, and are self-tracking as well (i.e., traits maintain both global and tag-based adoption counts).

* The "rules" by which agent state (and potentially, structure) evolves during the course of a simulation must allow individual-based heterogeneity. TransmissionLab, as with many simple CT and copying simulations, defined a single rule or rules for each simulation model. TF must allow both a globally defined rule (say, implement a classic Wright-Fisher model), and the opposite extreme, where each agent has a slightly different rule (or parameterization of a rule).

* Rule heterogeneity should be easy to relate to population structure through the tagging system, in order to allow easy statistical analysis of observables aggregated by agents sharing the same rule. An example would be a simulation where agents either use a conformist or an anti-conformist social learning rule, and are divided into multiple communities. TF should make it easy to tag agents by rule, and by community, and get a steady stream of adoption counts per trait, per trait by rule tag, per trait by community tag, and per trait by rule and community tag.


# Design Notes #

Everything below here is working notes, not necessarily requirements or specification.

## Population and Structural Abstractions ##

Major goals:

* Simple way to hold a set of IAgents, whether the entire set of existing agents, or an arbitrary partition of agents.

* Simple way to construct, enumerate, and use hierarchical subsets. It's really the tagging mechanism that should be used to arbitrary partitions, so the notion of a subpopulation is really a fairly limited one here. And perhaps it should be more of a "dynamic" things -- from the overall population, perhaps you can ask for a subpopulation composed of agents having a specific tag. That way, if you had a metapopulation model, agents belonging to the Ith deme would all share a specific tag for "deme-i" or something similar.

* Populations should facilitate common simulation operations; e.g., the selection of an agent at random from the subpopulation, determine its size, and so on.

* The presence of structured relations between agents are always represented by mathematical graph abstractions. The framework places no restrictions on the type of network or graph, or even the number of graphs that can represent relationships, except that any libraries must follow (or be adaptable to) the JUNG2 basic Graph interface conventions. Basic implementations coded within the framework use JUNG2 directly. This means that simple graphs, directed graphs, or even weighted directed hypergraphs are possible as abstractions for various types of structured agents relationships.

* Structure, not population, is the basic means of *traversing* agents within the population. Only certain "bulk" activities discussed above (e.g., select an agent at random, add a tag to all agents, querying for a subset of agents via predicate chains) use the Population abstraction.

# Implementation Notes #

##Guice/DI Notes##

* Once I switched the agent creation API from Model to IPopulation, there were test failures which were mysterious. This was tough to track down, but ultimately related to the fact that JUnit4 was creating multiple model objects whenever one was needed, so initializing the population within the model object was causing NPEs because new model objects were always in use. Models need to be created in the Singleton class for Guice if they're going to hold the persistent population object.
