package YakaC.Exception;

import YakaC.Parser.Ident;

public class CallException extends YakaException
{
  protected String m_name;
  protected Ident m_ident;

  public CallException(String name, Ident ident)
  {
    m_name = name;
    m_ident = ident;
  }

  public String toString()
  {
    return "Call error: Cannot call '"
      + m_name + "' which is a " + m_ident.kind()
      + ", expected a " + Ident.Kind.Function;
  }
}
