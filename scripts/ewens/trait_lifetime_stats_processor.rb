require_relative 'debug'

class TraitLifetimeStatsProcessor
  VERSION = '1.0.0'

    def initialize()
      @log = Debug.get_logger()
      @result_log = File.open('unified-trait-lifetime-data', "w")
      @log.debug("result log #{@result_log}")

      @result_log.puts("Model\tTheta\tPopsize\tMutation\tConformismRate\tMeanLifetime\tStdevLifetime\tMinLifetime\tMaxLifetime\tSampleSize")

    end

  def post_process(directory)
      @orig_directory = directory
      @log.info("Processing all output in: " + directory)
      Dir.chdir(directory)
      Dir.glob("*-tfout") do |dir|
        if(File.directory?(dir) == false)
          @log.debug("dir is not a directory, skipping: #{dir}")
          next
        end
        #next unless File.directory?(dir)
        next if (dir.eql?('.') || dir.eql?('..'))

        @log.debug("Processing subdirectory: #{dir}")
        process_subdir(dir)
      end
  end

  def process_subdir(dir)
    @log.info("PROCESSING DIRECTORY:#{dir} ============================")
    Dir.chdir(dir)
    params = get_run_params_from_dirname(dir)
    @log.debug(pp params)


    file = "trait-lifetime-stats"

    process_lifetime_stats(file, params)


    Dir.chdir(@orig_directory)
  end

  def get_run_params_from_dirname(d)
    params = Hash.new

    # typical directory name is:   WrightFisherDrift-1000-0.000005-100-3000-0.0-INF-1330037790521-tfout
    # which means <modelname>-<numagents>-<mutationrate>-<initialtraitnum>-<length>-<conformism rate>-mutationtype-<uniqueid>-tfout
    #@log.debug("pattern matching on dir: #{d}")
    d =~ /([a-zA-Z]+)-(\d+)-([\w|\.]+)-(\d+)-(\d+)-([\w|\.]+)-\S+-(\d+)/
    params['model'] = String($1)
    params['popsize'] = String($2)
    params['mutation'] = String($3)
    params['runid'] = String($7)
    params['conformism'] = String($6)

    theta = Float(params['mutation']) * Float(params['popsize']) * 2.0
    params['theta'] = theta

    params
  end


  def process_lifetime_stats(filename, params)
    # initialize variables
    mean = 0.0
    stdev = 0.0
    min = 0.0
    max = 0.0
    samplesize = 0

    file = File.open(filename, "r") do |file|
      while line = file.gets
        # need a case statement with regexes here...
        case line
          when /^mean:\s+([\w|\.]+)\s+/
            mean = $1.to_f
          when /^std dev:\s+([\w|\.]+)\s+/
            stdev = $1.to_f
          when /^min:\s+([\w|\.]+)\s+/
            min = $1.to_f
          when /^max:\s+([\w|\.]+)\s+/
            max = $1.to_f
          when /^n:\s+([\w|\.]+)\s+/
            samplesize = $1.to_i
          else
            next
        end

      end
    end

    @result_log.puts("#{params['model']}\t#{params['theta']}\t#{params['popsize']}\t#{params['mutation']}\t#{params['conformism']}\t#{mean}\t#{stdev}\t#{min}\t#{max}\t#{samplesize}")

  end

end