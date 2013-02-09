class BootstrapSlatkinTest

  def initialize(skip_unaveraged)
    @skip_unaveraged = skip_unaveraged
    @log = Debug.get_logger()
    @result_log = File.open('bootstrap-slatkin-input-data.txt', "w")
    @log.debug("result log: #{@result_log}");


  end





end