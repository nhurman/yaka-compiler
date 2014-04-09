package YakaC.Exception;

import YakaC.Parser.Ident.Type;

/**
 * Condition statement does not eval to a boolean
 */
public class ConditionException extends YakaException
{
  protected Type m_type; /**< Expression type */

  /**
   * @param type Expression type
   */
  public ConditionException(Type type)
  {
    m_type = type;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Condition error: expected Boolean expression, got " + m_type;
  }
}
