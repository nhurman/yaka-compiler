package YakaC;

public class IdConst extends Ident
{
  public IdConst(Type type, int value)
  {
    super(type, value);
    m_kind = Kind.Constant;
  }

  public String toString()
  {
    String v = "" + value();
    if (Ident.Type.Boolean == type())
      v = Ident.Boolean.str[value() - Ident.Boolean.True];

    return "[Constant] " + super.toString() + ": " + v;
  }
}
