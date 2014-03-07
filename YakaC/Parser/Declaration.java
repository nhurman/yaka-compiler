package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Ident.Boolean;
import YakaC.Event.EventManager;
import YakaC.Exception.RedeclaredIdentException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

public class Declaration
{
  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TabIdent m_tabIdent;
  protected int m_index;
  protected String m_name;
  protected Type m_type;

  public Declaration(ErrorBag errors, EventManager eventManager, TabIdent tabIdent)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_tabIdent = tabIdent;
    m_index = 0;
    m_name = null;
    m_type = null;
  }

  /** Constants **/
  public void constant(String name) throws RedeclaredIdentException
  {
    if (m_tabIdent.exists(name)) {
      m_errors.add(new RedeclaredIdentException(name));
      name = null;
    }

    m_name = name;
  }

  public void constant(Type type, int value) throws RedeclaredIdentException
  {
    add(new IdConst(type, value));
  }

  public void constant(Type type, boolean b) throws RedeclaredIdentException
  {
    add(new IdConst(type, b ? Boolean.True : Boolean.False));
  }

  public void constant(Ident ident) throws RedeclaredIdentException
  {
    add(new IdConst(ident.type(), ident.value()));
  }

  private void add(IdConst ident) throws RedeclaredIdentException
  {
    if (null == m_name)
      return;

    m_tabIdent.add(m_name, ident);
    m_name = null;
  }

  /** Variables **/
  public void variable(Type type)
  {
    m_type = type;
  }

  public void variable(String name) throws RedeclaredIdentException
  {
    m_index -= YVM.StackValueSize;
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
    return -(m_index / YVM.StackValueSize);
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