package YakaC.Exception;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Expression.Operator;

/**
 * Trying to use an operator on incompatible types
 */
public class TypeMismatchException extends YakaException
{
  protected Operator m_op; /**< Operator */
  protected Type m_t1; /**< First operand type */
  protected Type m_t2; /**< Second operand type */

  /**
   * @param op Operator
   * @param t1 First operand type
   * @param t2 Second operand type, ignored if op is unary
   */
  public TypeMismatchException(Operator op, Type t1, Type t2)
  {
    m_op = op;
    m_t1 = t1;
    m_t2 = t2;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    String out = "Type mismatch: got (";

    if (m_op.unary()) {
      out += m_op + " " + m_t1;
    }
    else {
      out += m_t1 + " " + m_op + " " + m_t2;
    }

    return out + ")";
  }
}
