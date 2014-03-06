package Yaka;

public class Ident
{
  public static enum Type {
    Error,
    Boolean,
    Integer;

    public static final String[] str = {
      "Error", "Boolean", "Integer"
    };
  };

  public static class Boolean {
    public static final int True = -1;
    public static final int False = 0;

    public static final String[] str = {
      "VRAI", "FAUX"
    };
  }

  protected Type m_type;
  protected int m_value;

  public Ident(Type type)
  {
    m_type = type;
    m_value = 0;
  }

  public Ident(Type type, int value)
  {
    m_type = type;
    m_value = value;
  }

  public int value()
  {
    return m_value;
  }

  public Type type()
  {
    return m_type;
  }

  public String toString()
  {
    return Type.str[m_type.ordinal()];
  }
}
