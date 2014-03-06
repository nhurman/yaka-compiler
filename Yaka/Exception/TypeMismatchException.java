package Yaka.Exception;

import Yaka.Ident.Type;
import Yaka.Expression.Operator;

public class TypeMismatchException extends YakaException
{
  protected Type m_t1;
  protected Operator m_op;
  protected Type m_t2;

  public TypeMismatchException(Type t1, Operator op, Type t2)
  {
    m_t1 = t1;
    m_op = op;
    m_t2 = t2;
  }

  public String toString()
  {
    return "Type mismatch: got (" + Type.str[m_t1.ordinal()]
      + " " + Operator.str[m_op.ordinal()]
      + " " + Type.str[m_t2.ordinal()] + ")";
  }
}
