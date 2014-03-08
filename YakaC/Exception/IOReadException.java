package YakaC.Exception;

import YakaC.Parser.Ident;

public class IOReadException extends YakaException
{
  protected String m_name;
  protected Ident m_ident;

  public IOReadException(String name, Ident ident)
  {
    m_name = name;
    m_ident = ident;
  }

  public String toString()
  {
    String out = "I/O error: Cannot read into '" + m_name + "'";

    if (YakaC.Parser.Ident.Kind.Variable != m_ident.kind()) {
      out += ", operation not supported on " + m_ident.kind();
    }
    else if (YakaC.Parser.Ident.Type.Integer != m_ident.type()) {
      out += ", invalid type '" + m_ident.type() + "'";
    }

    return out;
  }
}
