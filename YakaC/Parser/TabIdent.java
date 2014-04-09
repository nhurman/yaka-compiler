package YakaC.Parser;

import YakaC.Exception.*;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * Identifiers table
 */
public class TabIdent
{
  protected Context m_context; /**< Yaka context */
  protected HashMap<String, Ident> m_idents; /**< Identifiers table */

  /**
   * Constructor
   * @param context Yaka context
   * @param size Size to pre-alloc
   */
  public TabIdent(Context context, int size)
  {
    m_context = context;
    m_idents = new HashMap<String, Ident>(size);
  }

  /**
   * Check if an identifier exists
   * @param key Identifier name
   * @return true if identifier was found
   */
  public boolean exists(String key)
  {
    return m_idents.containsKey(key);
  }

  /**
   * Find an identifier using its name
   * @param key Identifier name
   * @return Identifier
   */
  public Ident find(String key) throws UndefinedIdentException
  {
    if (!exists(key)) {
      m_context.errorBag().add(new UndefinedIdentException(key));
      return new Ident();
    }

    return m_idents.get(key);
  }

  /**
   * Add an identifier if it does not already exist
   * @param key Identifier name
   * @param val Identifier
   */
  public void add(String key, Ident val) throws RedeclaredIdentException
  {
    if (exists(key)) {
      m_context.errorBag().add(new RedeclaredIdentException(key));
    }
    else {
      set(key, val);
    }
  }

  /**
   * Add an identifier, overwrite it if it already exists
   * @param key Identifier name
   * @param val Identifier
   */
  public void set(String key, Ident val)
  {
    m_idents.put(key, val);
  }

  /**
   * Count the identifiers in this table
   * @param kind Identifier kind
   * @param sign If not null, only count identifiers which values are the same sign as sign
   * @return Number of identifiers
   */
  public int count(Ident.Kind kind, int sign)
  {
    int num = 0;
    for (Ident ident: m_idents.values()) {
      if (kind == ident.kind()) {
        if (0 == sign ||
           (0 < sign && 0 <= ident.value()) ||
           (0 > sign && 0 >  ident.value())) {
          ++num;
        }
      }
    }

    return num;
  }

  /**
   * Count the identifiers in this table
   * @param kind Identifier kind
   * @return Number of identifiers
   */
  public int count(Ident.Kind kind)
  {
    return count(kind, 0);
  }

  /**
   * Get all the identifiers in this table
   * @return Identifiers collection
   */
  public Collection<Ident> all()
  {
    return m_idents.values();
  }

  /**
   * Clear the table, deleting all the identifiers
   */
  public void clear()
  {
    m_idents.clear();
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    String out = "";
    for (Map.Entry<String, Ident> item: m_idents.entrySet()) {
      out += "<" + item.getKey() + "> " + item.getValue() + "\n";
    }

    return out;
  }
}
