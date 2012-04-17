#!/usr/bin/env ruby
require_relative 'slatkin_exact_multiple'
require 'optparse'
require 'pp'

options = {}
optparse = OptionParser.new do |opts|
  opts.banner = "Usage: generate-sim-script.rb [options]"

  opts.on("-s", "--skip-unaveraged-data", "Flag to skip processing unaveraged data") do |s|
    options[:skipunavd] = s
  end
end

optparse.parse!

pp options


slatkin = SlatkinExactMultiple.new(options[:skipunavd])
slatkin.post_process(ARGV[0])
