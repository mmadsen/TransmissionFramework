#!/usr/bin/env ruby
require_relative 'slatkin_exact_multiple'


slatkin = SlatkinExactMultiple.new
slatkin.post_process(ARGV[0])
