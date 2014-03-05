package Yaka;

public abstract class Ident
{
  public static enum Type {
    Boolean,
    Integer
  };

  protected Type m_type;
  protected int m_value;

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
    String out = "";
    switch (m_type) {
      case Boolean: out += "Boolean"; break;
      case Integer: out += "Integer"; break;
      default: out += "UNKNOWN";
    }

    return out;
  }
}
