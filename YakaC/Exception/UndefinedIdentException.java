package YakaC.Exception;

/**
 * Trying to use an undefined identifier
 */
public class UndefinedIdentException extends YakaException
{
  protected String m_name; /**< Identifier name */

  /**
   * @param name Identifier name
   */
  public UndefinedIdentException(String name)
  {
    m_name = name;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Undefined identifier: " + m_name;
  }
}
