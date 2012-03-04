#!/usr/bin/env ruby
require 'optparse'
require_relative 'debug'

log = Debug.get_logger()

options = {}
optparse = OptionParser.new do |opts|

  opts.banner = "Usage: generate-sim-script.rb [options]"

  opts.on("-n", "--number-agents NUM", Integer, "Number of agents in the population") do |n|
    options[:numagents] = n
  end

  opts.on("-l", "--low-theta-value NUM", Float, "Low end of desired theta range") do |l|
    options[:lowtheta] = l
  end

  opts.on("-h", "--high-theta-value NUM", Float, "High end of desired theta range") do |h|
    options[:hightheta] = h
  end

  opts.on("-i", "--theta-increment NUM", Float, "Desired theta increments") do |i|
    options[:thetaincr] = i
  end

  options[:ewensincr] = 100
  opts.on("-e", "--size-ewens-sample NUM", Integer, "Size of Ewens samples for each time increment") do |e|
    options[:ewensincr]  = e
  end

  options[:length] = 5000
  opts.on("-t", "--length-sim-ticks NUM", Integer, "Length of the sim in model steps or ticks") do |t|
    options[:length] = t
  end

  options[:conf] = 0.1
  opts.on("-c", "--conformism-rate NUM", Float, "Rate of conformist copying compared to random copying events") do |c|
    options[:conf] = c
  end

  options[:startingtraits] = 100
  opts.on("-s", "--starting-traits NUM", Integer, "Number of traits to seed population initially") do |s|
    options[:startingtraits] = s
  end

  options[:transient] = 750
  opts.on("-d", "--start-collect-data NUM", Integer, "Begin collecting data at time index") do |d|
    options[:transient] = d
  end

  opts.on("-H", "--help", "Show help message") do
    puts opts
    exit
  end
end

optparse.parse!

#p options


curtheta = options[:lowtheta]
while(curtheta <= options[:hightheta])
    mu = curtheta / (2 * options[:numagents])
    #log.debug("curtheta: #{curtheta}  mu: #{mu}")


    print("java -Xms1g -Xmx1g -Dlog4j.configuration=log4j.config -ea -cp TransmissionFrameworkModels.jar:annotations.jar:aopalliance-1.0.jar:atunit-1.0.1.jar:collections-generic-4.01.jar:colt-1.2.0.jar:commons-cli-1.2.jar:commons-collections-3.2.1.jar:commons-math-2.2.jar:concurrent-1.3.4.jar:guava-r05.jar:guice-2.0.jar:jung-algorithms-2.0.1.jar:jung-api-2.0.1.jar:jung-graph-impl-2.0.1.jar:jung-io-2.0.1.jar:junit-4.8.1.jar:log4j-1.2.12.jar:stax-api-1.0.1.jar:trove4j-3.0.2.jar:wstx-asl-3.2.6.jar:xpp3_min-1.1.3.4.O.jar:xstream-1.2.2.jar org.madsenlab.sim.tf.test.examples.WrightFisherDriftModel.WrightFisherDriftSim -n #{options[:numagents]} -i -s #{options[:startingtraits]} -m #{mu} -l #{options[:length]} -p ./tf.properties -t #{options[:transient]} -e #{options[:ewensincr]}")
    print("\n")

    curtheta = curtheta + options[:thetaincr]
end







