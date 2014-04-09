package YakaC.Exception;

import YakaC.Parser.Ident;

/**
 * Trying to use the assign operator on something else than a variable
 */
public class AffectationException extends YakaException
{
  protected String m_name; /**< Identifier name */
  protected Ident m_ident; /**< Identifier */

  /**
   * @param name Identifier name
   * @param ident Identifier
   */
  public AffectationException(String name, Ident ident)
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
    return "Affectation error: Tried to assign a value to '"
      + m_name + "' which is a " + m_ident.kind();
  }
}
