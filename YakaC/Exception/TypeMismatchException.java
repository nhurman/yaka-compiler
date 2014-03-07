package YakaC.Exception;

import YakaC.Operation;
import YakaC.Ident.Type;
import YakaC.Expression.Operator;

public class TypeMismatchException extends YakaException
{
  protected Operation m_op;

  public TypeMismatchException(Operation op)
  {
    m_op = op;
  }

  public String toString()
  {
    return "Type mismatch: got (" + Type.str[m_op.t1.ordinal()]
      + " " + Operator.str[m_op.op.ordinal()]
      + " " + Type.str[m_op.t2.ordinal()] + ")";
  }
}
