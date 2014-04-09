package YakaC.Exception;

/**
 * Parse error thrown by the javacc analyzer
 */
public class ParseException extends YakaException
{
  protected String m_message; /**< Exception message */

  /**
   * @param message Exception message
   */
  public ParseException(String message)
  {
    m_message = message;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Parse error: " + m_message;
  }
}
