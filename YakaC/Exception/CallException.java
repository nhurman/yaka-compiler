package YakaC.Exception;

import YakaC.Parser.Ident;

/**
 * Trying to call something else than a function
 */
public class CallException extends YakaException
{
  protected String m_name; /**< Identifier name */
  protected Ident m_ident; /**< Identifier */

  /**
   * @param name Identifier name
   * @param ident Identifier
   */
  public CallException(String name, Ident ident)
  {
    m_name = name;
    m_ident = ident;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Call error: Cannot call '"
      + m_name + "' which is a " + m_ident.kind()
      + ", expected a " + Ident.Kind.Function;
  }
}
