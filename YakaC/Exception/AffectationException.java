package YakaC.Exception;

import YakaC.Parser.Ident;

public class AffectationException extends YakaException
{
  protected String m_name;
  protected Ident m_ident;

  public AffectationException(String name, Ident ident)
  {
    m_name = name;
    m_ident = ident;
  }

  public String toString()
  {
    return "Affectation error: Tried to assign a value to '"
      + m_name + "' which is a " + m_ident.kind();
  }
}
