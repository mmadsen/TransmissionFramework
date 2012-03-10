#!/usr/bin/env ruby
require_relative 'ewens_sample_kn_processor'


ewens = EwensSampleKnProcessor.new
ewens.post_process(ARGV[0])