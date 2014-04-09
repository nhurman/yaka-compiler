package YakaC.Parser;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Function representation
 */
public class IdFunct extends Ident
{
  /** Parameters list */
  protected ArrayDeque<Type> m_parameters;

  /**
   * Constructs an empty function
   */
  public IdFunct()
  {
    super(null, 0);
    m_kind = Kind.Function;
    m_parameters = new ArrayDeque<Type>();
  }

  /**
   * Constructs an empty function with a return type
   */
  public IdFunct(Type type)
  {
    super(type, 0);
    m_kind = Kind.Function;
    m_parameters = new ArrayDeque<Type>();
  }

  /**
   * Add a parameter
   * @param type Parameter type
   */
  public void addParameter(Type type)
  {
    m_parameters.add(type);
    ++m_value;
  }

  /**
   * Get a list of the parameters
   * @return Parameters
   */
  public final ArrayDeque<Type> getParameters()
  {
    return m_parameters;
  }

  /**
   * Check if two functions' signatures match
   * @param other Second function
   * @return true if both functions have the same ssignature
   */
  public boolean equals(IdFunct other)
  {
    if (null != m_type
     && null != other.m_type
     && !m_type.equals(other.m_type)) {
      return false;
    }

    Iterator i1 = m_parameters.iterator(),
             i2 = other.m_parameters.iterator();

    while (i1.hasNext() && i2.hasNext()) {
      if (!i1.next().equals(i2.next())) {
        return false;
      }
    }

    return !(i1.hasNext() || i2.hasNext());
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    String params = "";
    for (Type type: m_parameters) {
      params += type.toString() + ", ";
    }

    if (params.length() > 0) {
      params = params.substring(0, params.length() - 2);
    }

    return "[Function] " + super.toString() + "(" + params +")";
  }
}
