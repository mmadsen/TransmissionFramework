require_relative 'debug'

class SlatkinExactMultiple
  VERSION = '1.0.0'

  def initialize()
    @log = Debug.get_logger()
    @result_log = File.open('unified-slatkin-results-by-windowsize-and-gen', "w")
    @log.debug("result log: #{@result_log}");
    @result_log.puts("Model\tPopsize\tMutation\tWindowsize\tGeneration\tSampleConfig\tSlatkinExactP")

  end

  def post_process(directory)
    @orig_directory = directory
    @log.info("Processing all output in: " + directory)
    Dir.chdir(directory)
    Dir.glob("Wright*") do |dir|
      if(File.directory?(dir) == false)
        @log.debug("dir is not a directory, skipping: #{dir}")
        next
      end
      #next unless File.directory?(dir)
      next if (dir.eql?('.') || dir.eql?('..'))

      @log.debug("Processing subdirectory: #{dir}")
      process_files_in_subdir(dir)
    end
  end

  def process_files_in_subdir(directory)
    @log.info("PROCESSING DIRECTORY:#{directory} ============================")
    Dir.chdir(directory)
    params = get_run_params_from_dirname(directory)
    @log.debug(pp params)

    files = Dir.glob("ewens-sample-ta-*")
    files << 'ewens-sampling-distro-by-time'

    files.each do |file|
      next if file == nil

      @log.debug("===== processing #{file} ================")
      calculate_and_log_slatkin_exact(file, params)
    end
    Dir.chdir(@orig_directory)
  end

  def get_run_params_from_dirname(d)
    params = Hash.new

    # typical directory name is:   WrightFisherDrift-2000-0.0010-100-3000-INF-1329859120382
    # which means <modelname>-<numagents>-<mutationrate>-<initialtraitnum>-<length>-<mutationtype>-<uniqueid>
    #@log.debug("pattern matching on dir: #{d}")
    d =~ /([a-zA-Z]+)-(\d+)-([\w|\.]+)\S+-(\d+)/
    params['model'] = String($1)
    params['popsize'] = String($2)
    params['mutation'] = String($3)
    params['runid'] = String($4)

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
        #@log.debug("#{params['model']}\t#{params['popsize']}\t#{params['mutation']}\t#{windowsize} gen #{gen} with config #{config}")

        # get the P(E) result from slatkin exact

        result = `~/bin/slatkin-pe-only 100000 #{config}`
        #@log.debug("slatkin result: #{result}")

        @result_log.puts("#{params['model']}\t#{params['popsize']}\t#{params['mutation']}\t#{windowsize}\t#{gen}\t#{config}\t#{result}")
      end
    end
  end

end