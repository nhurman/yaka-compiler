package YakaC.Exception;

import YakaC.Parser.Ident.Type;

public class IterationException extends YakaException
{
  protected Type m_type;

  public IterationException(Type type)
  {
    m_type = type;
  }

  public String toString()
  {
    return "Loop error: expected condition to be Boolean, got " + m_type;
  }
}
