package YakaC;

public class IdVar extends Ident
{
  public IdVar(Type type, int value)
  {
    super(type, value);
    m_kind = Kind.Variable;
  }

  public String toString()
  {
    return "[Variable] " + super.toString() + " offset: " + value();
  }
}
