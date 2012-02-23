require 'logger'
require 'pp'

module Debug
  def Debug.get_logger
    logger = Logger.new(STDERR)
    logger.level = Logger::DEBUG

    logger
  end

end
