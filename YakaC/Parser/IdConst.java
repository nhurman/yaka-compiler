package YakaC.Parser;

/**
 * Constant representation
 */
public class IdConst extends Ident
{
  /**
   * Constructor
   * @param type Type
   * @param value Value
   */
  public IdConst(Type type, int value)
  {
    super(type, value);
    m_kind = Kind.Constant;
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    String v = "" + value();
    if (Ident.Type.Boolean == type())
      v = Ident.Boolean.str[value() - Ident.Boolean.True];

    return "[Constant] " + super.toString() + ": " + v;
  }
}
