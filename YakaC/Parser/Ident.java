package YakaC.Parser;

/**
 * Identifier representation
 */
public class Ident
{
  /**
   * Identifier type
   */
  public static enum Type {
    Error,
    Boolean,
    Integer;
  };

  /**
   * Identifier kind
   */
  public static enum Kind {
    Undefined,
    Constant,
    Variable,
    Function;
  };

  /**
   * Boolean type representation
   */
  public static class Boolean {
    public static final int True = -1;
    public static final int False = 0;

    public static final String[] str = {
      "True", "False"
    };
  }

  protected Type m_type; /**< Identifier type */
  protected Kind m_kind; /**< Identifier kind */
  protected int m_value; /**< Identifier value */

  /**
   * Builds an empty ident
   */
  public Ident()
  {
    m_type = Type.Error;
    m_value = 0;
    m_kind = Kind.Undefined;
  }

  /**
   * Builds aan ident with a type
   * @param type Type
   */
  public Ident(Type type)
  {
    m_type = type;
    m_value = 0;
    m_kind = Kind.Undefined;
  }

  /**
   * Builds an ident with a type and a value
   * @param type Type
   * @param value Value
   */
  public Ident(Type type, int value)
  {
    m_type = type;
    m_value = value;
    m_kind = Kind.Undefined;
  }

  /**
   * Get the identifier's value
   * @return Value
   */
  public int value()
  {
    return m_value;
  }

  /**
   * Set the identifier's value
   * @param value Value
   */
  public void value(int value)
  {
    m_value = value;
  }

  /**
   * Get the identifier's type
   * @return Type
   */
  public Type type()
  {
    return m_type;
  }

  /**
   * Get the identifier's kind
   * @return Kind
   */
  public Kind kind()
  {
    return m_kind;
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    return (null != m_type) ? m_type.toString() : "";
  }
}
