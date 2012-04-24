require_relative 'debug'

class CalculateTfEwensSample
  VERSION = '1.0.0'

  def initialize()
    @log = Debug.get_logger()
    @result_log = File.open('unified-tf-by-windowsize-and-gen', "w")
    @log.debug("result log: #{@result_log}")
    @result_log.puts("Model\tTheta\tWindowsize\tConformismRate\tSampleConfig\tSlatkinExactP\tThetaEst\tTfEst\tIQV\tShannonWeaver")

  end



  def process(filename)
    @log.info("PROCESSING FILE: #{filename} ======================")
    file = File.open(filename, "r") do |file|
      while line = file.gets
        # ignore header
        next if line =~ /^Model\sTheta/
        @log.debug("=========================== processing line ===============================")
        # split all other lines on \s
        logline = String(line)
        # example log line:  WrightFisherDrift       0.1     2000    2.5e-05 1000    0.0     22001   98 1 1  1.0     0.431591
        logline =~ /([a-zA-Z]+)\s([\w|\.]+)\s([\w|\.]+)\s[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?\s([\w|\.]+)\s([\w|\.]+)\s([\w|\.]+)\s(.+)$/

        model = String($1)
        theta = String($2)
        popsize = String($3)
        mutation = String($4)
        window = String($5)
        conformism = String($6)
        gen = String($7)
        rest = String($8)

        #@log.debug("rest: #{rest}")

        rest =~ /^(.+?)\s([\w|\.]+)\s([\w|\.]+)$/
        config = String($1)
        pe = String($2)
        thetaest = String($3)




        #@log.debug("=== config: #{config} pe:#{pe} te: #{thetaest}======")

        configString = String(config)
        tokens = config.split(/\s/)
        total = 0.0
        k = 0.0

        # first calculate total sample size, to use in calculating frequencies
        tokens.each do |token|
          count = Float(token)
          total += count
          k += 1
        end

        @log.debug("total sample size: #{total}  k:  #{k}")

        # now calculate the Tf value
        denom = 0.0
        shannon = 0.0

        tokens.each do |token|
          count = Float(token)
          freq = count / total
          @log.debug("freq: #{freq}")
          denom += freq**2
          shannon += (freq * Math.log(freq))
        end

        # finish calculating Shannon Weaver
        sw = shannon * -1

        tf = (1 / denom) - 1
        @log.debug("tf calc: denominator: #{denom}   tf value: #{tf}")
        num = 0.0

        # calculate Eerken's IQV value too, are they the same?
        if(k > 1)
          num = k / (k-1)
          iqv = num * (1 - denom)
          @log.debug("iqv calc: num: #{num} iqv: #{iqv} ")
        else
          iqv = 0.0
        end






        # here's the structure of the original log file, for reference on fields
        # #{params['model']}\t#{params['theta']}\t#{params['popsize']}\t#{params['mutation']}\t#{windowsize}\t#{params['conformism']}\t#{gen}\t#{config}\t#{pe}\t#{theta_est}")

        @result_log.puts("#{model}\t#{theta}\t#{window}\t#{conformism}\t#{config}\t#{pe}\t#{thetaest}\t#{tf}\t#{iqv}\t#{sw}")


      end
    end
  end


end