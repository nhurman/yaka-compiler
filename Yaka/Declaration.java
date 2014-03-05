package Yaka;

import Yaka.Exception.*;

public class Declaration
{
  protected TabIdent m_tabIdent;
  protected int m_index;
  protected String m_name;

  protected class Bool {
    public static final int True = -1;
    public static final int False = 0;
  }

  public Declaration()
  {
    m_tabIdent = new TabIdent(0);
    m_index = -2;
    m_name = null;
  }

  public void constant(String name) throws RedeclaredIdentException
  {
    if (m_tabIdent.exists(name))
      throw new RedeclaredIdentException();

    m_name = name;
  }

  public void constant(Ident.Type type, int value) throws RedeclaredIdentException
  {
    add(new IdConst(type, value));
  }

  public void constant(Ident.Type type, boolean b) throws RedeclaredIdentException
  {
    add(new IdConst(type, b ? Bool.True : Bool.False));
  }

  public void constant(Ident ident) throws RedeclaredIdentException
  {
    add(new IdConst(ident.type(), ident.value()));
  }

  private void add(IdConst ident) throws RedeclaredIdentException, RuntimeException
  {
    if (null == m_name)
      throw new RuntimeException();

    m_tabIdent.add(m_name, ident);
    m_name = null;
  }

  public Ident get(String name) throws UndefinedIdentException
  {
    return m_tabIdent.find(name);
  }

  public String toString()
  {
    return m_tabIdent.toString();
  }
}
