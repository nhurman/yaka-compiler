package YakaC.Exception;

import YakaC.Parser.Ident.Type;

/**
 * Type mismatch between return and function signature
 */
public class ReturnTypeException extends YakaException
{
  protected Type m_t1; /**< Function type */
  protected Type m_t2; /**< Return type */

  /**
   * @param t1 Function type
   * @param t2 Return type
   */
  public ReturnTypeException(Type t1, Type t2)
  {
    m_t1 = t1;
    m_t2 = t2;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Function signature says it should return " + m_t1 + " but got " + m_t2;
  }
}
