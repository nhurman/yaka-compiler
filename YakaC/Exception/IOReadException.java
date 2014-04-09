package YakaC.Exception;

import YakaC.Parser.Ident;

/**
 * Incompatible types for I/O operations
 */
public class IOReadException extends YakaException
{
  protected String m_name; /**< Identifier name */
  protected Ident m_ident; /**< Identifier */

  /**
   * @param name Identifier name
   * @param ident Identifier
   */
  public IOReadException(String name, Ident ident)
  {
    m_name = name;
    m_ident = ident;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
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
