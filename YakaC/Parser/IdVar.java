package YakaC.Parser;

/**
 * Variable representation
 */
public class IdVar extends Ident
{
  /**
   * Constructor
   * @param type Type
   * @param value Value
   */
  public IdVar(Type type, int value)
  {
    super(type, value);
    m_kind = Kind.Variable;
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    return "[Variable] " + super.toString() + " offset: " + value();
  }
}
