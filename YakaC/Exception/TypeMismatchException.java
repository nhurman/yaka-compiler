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
    return "Type mismatch: got (" + m_op.t1
      + " " + m_op.op + " " + m_op.t2 + ")";
  }
}
