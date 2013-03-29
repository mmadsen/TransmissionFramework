# Contact #

Please send all queries related to TransmissionFramework to:  mark AT madsenlab.org


# Introduction #

TransmissionFramework (TF) is a Java library and simulation framework for constructing agent-based simulation models of cultural transmission and social learning phenomena.

TF is the next generation of my TransmissionLab system, which had some design limitations which prevented the easy construction of models with individual-level heterogeneity in transmission rules. TransmissionLab also relied upon RepastJ, an agent-based simulation framework. TF does not, and is intended to be used in a variety of simulation contexts. TF is also intended to be thread-safe, so it can be used to write concurrent simulations (although this is not a primary goal).

Version 2.0 allows simulation models to be specified in an XML configuration file format, allowing the use of both framework classes for "default" behavior (e.g., Wright-Fisher and Moran dynamics are available out of the box), as well as user-supplied classes, and a mixture of both.  The framework supplies a default model class and main execution method, allowing simple cases to be simulated simply by editing configuration files.  User supplied classes are added to a model by including them in the classpath of the executing model, and specifying their fully qualified class names in the model's XML configuration.  

**TODO in Version 2.0:**  Need an extension point in SimulationConfigurationFactory to register user-supplied classes for parsing pieces of the configuration and returning user-supplied configuration objects as part of the ModelConfiguration object.  

# Major Design Goals #

* Minimize the amount of "tracking" and "accounting" work a simulation author needs to do. Make the "traits" (defined quite broadly) that agents adopt and which flow through the population self-counting under all circumstances.

* Allow simulation authors to easily set up multiple mesoscopic or macroscopic "observables" in their simulations, without having to understand the internals of the framework. Simulation authors should simply be able to register one of their objects as an Observer for a given observable, and the framework should provide a steady stream of frequency or occurrence counts.  The Observer is then responsible for calculating whatever macroscopic quantity needed from the stream of data.

* Provide explicit support for observation units that are derivative of traits and transmitted information in the simulation, to recognize the fact that we often don't observe the information actually being transmitted, but instead monitor its effects indirectly through classifications, measurement tools, or other means.  

* Avoid pre-defining the notion of "mesoscopic" structure, and allow this to be defined as much as possible by simulation authors. Clearly, spatial and network structure requires specific support in the framework code, and this is provided. But in addition, the framework should support arbitrary structure through the use of "tagging," both for agents and for traits. Tags serve as arbitrary partitions of the agent and trait universes, and are self-tracking as well (i.e., traits maintain both global and tag-based adoption counts).

* The "rules" by which agent state (and potentially, structure) evolves during the course of a simulation must allow individual-based heterogeneity. TransmissionLab, as with many simple CT and copying simulations, defined a single rule or rules for each simulation model. TF must allow both a globally defined rule (say, implement a classic Wright-Fisher model), and the opposite extreme, where each agent has a slightly different rule (or parameterization of a rule).

* Rule heterogeneity should be easy to relate to population structure through the tagging system, in order to allow easy statistical analysis of observables aggregated by agents sharing the same rule. An example would be a simulation where agents either use a conformist or an anti-conformist social learning rule, and are divided into multiple communities. TF should make it easy to tag agents by rule, and by community, and get a steady stream of adoption counts per trait, per trait by rule tag, per trait by community tag, and per trait by rule and community tag.

# Near Future Features #

The following are features which will be incorporated into near future commits to TransmissionFramework, in support of current research:

* Structured logging by observers to an external database, to facilitate tracking large numbers of simulation runs
* System for specifying a population structure model, both constant for a simulation run, and dynamic (e.g., new subpopulations arise and old subpopulations go extinct)
* System for specifying a demographic model, allowing changing population size
* Tagging of agents by the ruleset they possess, to allow observation and stats on a per-rule basis (e.g., frequency of traits among agents with rule X)

# Future Features #

The following are desired future features, in support of longer-term research goals:

* Examples of structured traits, like trees of ConceptNet objects, to allow examination of the evolution of structured information via normal CT processes
* Examples of game theoretic interaction rules
* 
