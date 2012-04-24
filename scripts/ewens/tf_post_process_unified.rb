require_relative 'debug'

class TfPostProcessUnified
  VERSION = '1.0.0'

  def initialize(skip_unaveraged)
    @skip_unaveraged = skip_unaveraged
    @log = Debug.get_logger()
    @result_log = File.open('tf-results-unified.txt', "w")
    @log.debug("result log: #{@result_log}");
    @result_log.puts("Model\tTheta\tPopsize\tMutation\tWindowsize\tConformismRate\tSampleConfig\tSlatkinExactP\tThetaEst\tKn\tMeanLifetime\StdevLifetime")

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



      # do all processing steps
      Dir.chdir(directory)
      params = get_run_params_from_dirname(directory)
      @log.info("PROCESSING DIRECTORY:#{directory} ============================")
      @log.debug(pp params)

      # do individual processing steps

      process_ewens_slatkin_files_in_subdir(dir)

      # change back, so we can process the next subdirectory
      Dir.chdir(@orig_directory)
    end
  end

  def process_ewens_slatkin_files_in_subdir(directory)


    files = Dir.glob("ewens-sample-ta-*")
    files << 'ewens-sampling-distro-by-time' unless @skip_unaveraged

    files.each do |file|
      next if file == nil

      @log.debug("===== processing #{file} ================")
      calculate_and_log_slatkin_exact(file, params)
    end
    #Dir.chdir(@orig_directory)
  end

  def process_kn_files_in_subdir(dir)
    @log.info("PROCESSING DIRECTORY:#{dir} ============================")
    Dir.chdir(dir)
    params = get_run_params_from_dirname(dir)
    @log.debug(pp params)

    files = Dir.glob("ewens-kn-count-ta-*")
    files << "ewens-kn-count-by-time"

    files.each do |file|
      next if file == nil

      @log.debug("===== processing #{file} ================")
      process_kn_values(file, params)
    end
    Dir.chdir(@orig_directory)
  end

  def get_run_params_from_dirname(d)
    params = Hash.new

    # typical directory name is:   WrightFisherDrift-1000-0.000005-100-3000-0.0-INF-1330037790521-tfout
    # which means <modelname>-<numagents>-<mutationrate>-<initialtraitnum>-<length>-<conformism rate>-mutationtype-<uniqueid>-tfout
    #@log.debug("pattern matching on dir: #{d}")
    d =~ /([a-zA-Z]+)-(\d+)-([\w|\.]+)-(\d+)-(\d+)-([\w|\.]+)-\S+-(\d+)/
    params['model'] = String($1)
    params['popsize'] = String($2).to_i
    params['mutation'] = String($3).to_f
    params['runid'] = String($7)
    params['conformism'] = String($6)

    theta = Float(params['mutation']) * Integer(params['popsize']) * 2.0
    params['theta'] = theta

    params
  end


  def calculate_and_log_slatkin_exact(filename, params)
    # capture the TA windowsize from the filename
    if(filename == "ewens-sampling-distro-by-time")
      windowsize = 1
    else
      filename =~ /.+-(\d+)$/
      windowsize = Integer($1)
    end

    # read the file, process each configuration, write results to new log
    file = File.open(filename, "r") do |file|
      while line = file.gets
        next if line =~ /^\w*$/  # ignore blank lines
        line =~ /(\d+)\s+(.+)$/
        gen = Integer($1)
        config = String($2)



        # get the P(E) result from slatkin exact

        result = `~/bin/slatkin-pe-theta 100000 #{config}`
        #@log.debug("slatkin result: #{result}")
        result =~ /([\w|\.]+)\s+([\w|\.]+)/
        pe = $1.to_f
        theta_est = $2.to_f
        #@log.debug("logging #{pe}\t#{theta_est}")


        @result_log.puts("#{params['model']}\t#{params['theta']}\t#{params['popsize']}\t#{params['mutation']}\t#{windowsize}\t#{params['conformism']}\t#{gen}\t#{config}\t#{pe}\t#{theta_est}")
      end
    end
  end

  def process_kn_values(filename, params)
    if(filename == "ewens-kn-count-by-time")
      windowsize = 1
    else
      filename =~ /.+-(\d+)$/
      windowsize = Integer($1)
    end


    @log.debug("windowsize: #{windowsize}")
    file = File.open(filename, "r") do |file|
      while line = file.gets
        #next if line =~ /^\w*$/
        line =~ /^(\d+)$/  # should be one integer per line...
        count = Integer($1)

        @result_log.puts("#{params['model']}\t#{params['theta']}\t#{params['popsize']}\t#{params['mutation']}\t#{windowsize}\t#{params['conformism']}\t#{count}")


      end
    end

  end

end