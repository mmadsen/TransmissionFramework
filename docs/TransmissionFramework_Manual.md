# TransmissionFramework Manual #

Author:  Mark E. Madsen
Email:  mark@madsenlab.org
Version:  1.4
Date:  2/19/12

## Overview ##



## Execution Environment##

* classpath and jar files for external libraries (unwritten)
* logging setup (unwritten, defaults work fine but don't allow others to debug or trace)
* running a model without the shell scripts
* need to provide Javadoc API documentation for framework, perhaps creating a zip out of the JD from the source tree.  


## Command Line Parameters ##

If a specific model is not listed after the command line switch, the given parameter applies to all models and must be implemented by model writers.

*  -n

   Number of agents (N) to be created at the beginning of the simulation model.  Currently, this number of agents is static throughout the model run, but future versions may allow different population histories.  

*  -s

   Number of starting traits to be randomly assigned to the initial agent population.  This number should be less than the number of agents, of course.  This number has little real effect except for the initial transient period before the Markov chain representing a given model reaches its "mixing" state and the amount of variation is controlled by model parameters and the model's stationary or quasi-stationary distribution.  It does have some interaction with the amount of time we wait before collecting statistics (see -t option, below, so generally I set -t to some larger value than -s, and perhaps a small multiple.  There are analytic results to help figure this out for the Moran model, but in general it's not well understood for all of the kinds of models we're doing here, so heuristics and some data observation is required when initially building a model).  

*  -m

   Mutation or innovation rate, measured in probability per copying event.  Thus, if we are running a Wright-Fisher model, with 1000 agents, and a 0.001 rate of innovation, we would expect an average of 1 innovation rate per generation, and 999 copying events without mutation.  Together with the population size (N), this defines the fundamental parameter of neutral or nearly neutral models, the "theta" parameter used by Ewens and other researchers.  

*  -l

   Length of the simulation run in model steps.  This number refers to the number of times the model increments the time clock.  This can refer, in Wright-Fisher models, to N copying events per step, or in Moran models, to 1 copying event per step, so the relation between this parameter and the underlying copying model must be defined (a model sets an enumerated constant inside the model code to indicate this mapping).  This parameter should be set in relation to N to give the desired number of **copying events** per simulation run.  For example, if we want to see each agent in a Wright-Fisher model with 1000 individuals undergo 2000 copying events (the first 100 to 150 perhaps representing transient behavior which is ignored), given that each model step represents N copying events, we set this parameter to 2000.  On the other hand, if we want a Moran model with 2000 copying events per individual, we would set this parameter to 2000 * 1000, or 2 million steps.  This does not multiply run time by a huge factor, of course, because each step in a WF model does many times the work of a step in a Moran model (although WF is comparatively more efficient to run given that accounting and statistics between steps is "batched" by a factor of N copying events).  

*  -t

   The length in model steps that the initial conditions are expected to last before the model settles down to stationary, mixing behavior.  No statistics are gathered for the duration of this parameter, from the beginning of the model.  For 1000 agents with 100 initial traits, 100-150 model steps appears to cover it in WF, remember that this would be 100K steps.  

*  -e 

   The size of a "sample" of each model tick or time averaging window to take, for comparison to the Ewens Sampling Distribution.  This number of agents are sampled randomly during each model step (and at the end of each time-averaging window) and the trait counts and trait richness recorded, for use in the Slatkin exact test and comparison directly with the ESD.  

*  -p

   The path and file name to a properties file which lists the log file names for various statistics, and the output directory where per-simulation-run directories and files are written.  It is usually only necessary to customize the logging directory, although you can customize filenames if desired.  DO NOT CHANGE ANY OF THE PROPERTY NAMES ON THE LEFT HAND SIDE OF EACH LINE -- the code relies on these property names and will exit with an error if you change them.  

*  -i

   Switch indicating that an infinite alleles model of innovation is desired.  In this case, there is no maximum number of traits that can be held in a single locus or dimension (although in practical terms, we have a fixed and very large constant for this size, which is changeable if desired).  

*  -c   (ConformistDriftSim)

   In "conformist" models of transmission, we specify the probability that an  agent, when executing a copying event, will decide to select the "most" or "least" frequent trait among its "neighbors" (who might be the whole population).  Typical values are 10% or 0.1, etc.  

*  -a   (ConformistDriftSim)

   By default, conformist models use the "most frequent" trait rule for copying.  Anti-conformist behavior can be selected by adding the -a switch to the command line, along with specifying the -c <rate> parameter.  In this case, whenever an agent executes a copying rule and conformism happens, anti-conformist behavior is selected.  
	
*  -d   (Metapopulation Models)

   The number of subpopulations or demes into which the population should be split.  NOT COMPLETE - further development required here.  

*  -f   (INCOMPLETE)

   As an alternative to the infinite alleles model, one can select a finite number of alleles in the population, using a combination of the -s and -f switches.  For example, to have a classic biallelic model, -s and -f should both be specified as 2.  Or we could have 2 starting traits, and allow mutation to eventually yield a maximum of 10.  This is incomplete because I need to implement backmutation rates in a non-infinite alleles context, but almost all of the basic plumbing is there.  

