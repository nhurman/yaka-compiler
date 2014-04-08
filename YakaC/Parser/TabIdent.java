package YakaC.Parser;

import YakaC.Exception.*;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class TabIdent
{
  protected ErrorBag m_errors;
  protected HashMap<String, Ident> m_idents;

  public TabIdent(ErrorBag errors, int size)
  {
    m_errors = errors;
    m_idents = new HashMap<String, Ident>(size);
  }

  public boolean exists(String key)
  {
    return m_idents.containsKey(key);
  }

  public Ident find(String key) throws UndefinedIdentException
  {
    if (!exists(key)) {
      m_errors.add(new UndefinedIdentException(key));
      return new Ident();
    }

    return m_idents.get(key);
  }

  public void add(String key, Ident val) throws RedeclaredIdentException
  {
    if (exists(key)) {
      m_errors.add(new RedeclaredIdentException(key));
    }
    else {
      set(key, val);
    }
  }

  public void set(String key, Ident val)
  {
    m_idents.put(key, val);
  }

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

  public int count(Ident.Kind kind)
  {
    return count(kind, 0);
  }

  public Collection<Ident> all()
  {
    return m_idents.values();
  }

  public void clear()
  {
    m_idents.clear();
  }

  public String toString()
  {
    String out = "";
    for (Map.Entry<String, Ident> item: m_idents.entrySet()) {
      out += "<" + item.getKey() + "> " + item.getValue() + "\n";
    }

    return out;
  }
}
