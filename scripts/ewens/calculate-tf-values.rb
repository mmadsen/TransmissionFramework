#!/usr/bin/env ruby
require_relative 'calculate_tf_ewens_sample'

tfproc = CalculateTfEwensSample.new()
tfproc.process(ARGV[0])

