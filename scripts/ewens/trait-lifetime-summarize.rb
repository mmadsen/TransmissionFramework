#!/usr/bin/env ruby
require_relative 'trait_lifetime_stats_processor'


life = TraitLifetimeStatsProcessor.new
life.post_process(ARGV[0])