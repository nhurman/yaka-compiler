package YakaC.Exception;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Expression.Operator;

public class TypeMismatchException extends YakaException
{
  protected Operator m_op;
  protected Type m_t1;
  protected Type m_t2;

  public TypeMismatchException(Operator op, Type t1, Type t2)
  {
    m_op = op;
    m_t1 = t1;
    m_t2 = t2;
  }

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
