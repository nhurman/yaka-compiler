package YakaC.Parser;

public class Ident
{
  public static enum Type {
    Error,
    Boolean,
    Integer;
  };

  public static enum Kind {
    Undefined,
    Constant,
    Variable,
    Function;
  };

  public static class Boolean {
    public static final int True = -1;
    public static final int False = 0;

    public static final String[] str = {
      "True", "False"
    };
  }

  protected Type m_type;
  protected Kind m_kind;
  protected int m_value;

  public Ident()
  {
    m_type = Type.Error;
    m_value = 0;
    m_kind = Kind.Undefined;
  }

  public Ident(Type type)
  {
    m_type = type;
    m_value = 0;
    m_kind = Kind.Undefined;
  }

  public Ident(Type type, int value)
  {
    m_type = type;
    m_value = value;
    m_kind = Kind.Undefined;
  }

  public int value()
  {
    return m_value;
  }

  public void value(int value)
  {
    m_value = value;
  }

  public Type type()
  {
    return m_type;
  }

  public Kind kind()
  {
    return m_kind;
  }

  public String toString()
  {
    return (null != m_type) ? m_type.toString() : "";
  }
}
