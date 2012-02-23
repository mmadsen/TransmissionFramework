require_relative 'debug'

class SlatkinExact
  VERSION = '1.0.0'

  def initialize(directory)
    @log = Debug.get_logger()
    @log.info("Processing directory: " + directory)

    Dir.chdir(directory)
    @files = Dir.glob("ewens-sample-ta-*")
    @files << 'ewens-sampling-distro-by-time'

    pp @files

    @result_log = File.open('unified-slatkin-results-by-windowsize-and-gen', "w")
    @log.debug("result log: #{@result_log}");

  end

  def process_directory
    @files.each do |file|
      next if file == nil

      @log.debug("==================================  processing #{file}")
      calculate_and_log_slatkin_exact(file)
    end
  end

  def calculate_and_log_slatkin_exact(filename)
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
        @log.debug("windowsize #{windowsize} gen #{gen} with config #{config}")

        # get the P(E) result from slatkin exact

        result = `~/bin/slatkin-pe-only 100000 #{config}`
        @log.debug("slatkin result: #{result}")

        @result_log.puts("#{windowsize}\t#{gen}\t#{config}\t#{result}")
      end
    end
  end

end