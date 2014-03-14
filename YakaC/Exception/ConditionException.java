package YakaC.Exception;

import YakaC.Parser.Ident.Type;

public class ConditionException extends YakaException
{
  protected Type m_type;

  public ConditionException(Type type)
  {
    m_type = type;
  }

  public String toString()
  {
    return "Condition error: expected Boolean expression, got " + m_type;
  }
}
