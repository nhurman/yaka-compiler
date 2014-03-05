package Yaka;

import Yaka.Exception.*;
import java.util.Map;
import java.util.HashMap;

public class TabIdent
{
  protected HashMap<String, Ident> m_idents;

  public TabIdent(int size)
  {
    m_idents = new HashMap<String, Ident>();
  }

  public boolean exists(String key)
  {
    return m_idents.containsKey(key);
  }

  public Ident find(String key) throws UndefinedIdentException
  {
    if (!exists(key))
      throw new UndefinedIdentException();

    return m_idents.get(key);
  }

  public void add(String key, Ident val) throws RedeclaredIdentException
  {
    if (exists(key))
      throw new RedeclaredIdentException();

    set(key, val);
  }

  public void set(String key, Ident val)
  {
    m_idents.put(key, val);
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
