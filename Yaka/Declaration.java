package Yaka;

import Yaka.Exception.*;

public class Declaration
{
  protected TabIdent m_tabIdent;
  protected int m_index;
  protected String m_name;
  protected Ident.Type m_type;

  protected static final int StackValueSize = 2;

  protected class Bool {
    public static final int True = -1;
    public static final int False = 0;
  }

  public Declaration(TabIdent tabIdent)
  {
    m_tabIdent = tabIdent;
    m_index = 0;
    m_name = null;
    m_type = null;
  }

  /** Constants **/
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

  /** Variables **/
  public void variable(Ident.Type type)
  {
    m_type = type;
  }

  public void variable(String name) throws RedeclaredIdentException
  {
    m_index -= StackValueSize;
    add(name, new IdVar(m_type, m_index));
  }

  private void add(String name, IdVar ident) throws RedeclaredIdentException, RuntimeException
  {
    if (null == m_type)
      throw new RuntimeException();

    m_tabIdent.add(name, ident);
  }

  public int countVariables()
  {
    return -(m_index / StackValueSize);
  }

  /** General purpose **/
  public Ident get(String name) throws UndefinedIdentException
  {
    return m_tabIdent.find(name);
  }

  public String toString()
  {
    return "-- Symbols table:\n" + m_tabIdent.toString();
  }
}
