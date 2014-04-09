package YakaC.Exception;

/**
 * Trying to redeclare an identifier
 */
public class RedeclaredIdentException extends YakaException
{
  protected String m_name; /**< Identifier name */

  /**
   * @param name Identifier name
   */
  public RedeclaredIdentException(String name)
  {
    m_name = name;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Redeclared identifier: " + m_name;
  }
}
