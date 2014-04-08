package YakaC.Exception;

import YakaC.Parser.Ident.Type;

public class ReturnTypeException extends YakaException
{
  protected Type m_t1;
  protected Type m_t2;

  public ReturnTypeException(Type t1, Type t2)
  {
    m_t1 = t1;
    m_t2 = t2;
  }

  public String toString()
  {
    return "Function signature says it should return " + m_t1 + " but got " + m_t2;
  }
}
