package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Ident.Boolean;
import YakaC.Event.*;
import YakaC.Exception.RedeclaredIdentException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

public class Declaration
{
  public static enum Event implements YakaC.Event.Event
  {
    Function;
  }

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TabIdent m_globals;
  protected TabIdent m_locals;
  protected int m_index;
  protected String m_name;
  protected Type m_type;
  protected IdFunct m_function;

  public Declaration(ErrorBag errors, EventManager eventManager, TabIdent globals, TabIdent locals)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_globals = globals;
    m_locals = locals;
    m_index = 0;
    m_name = null;
    m_type = null;
    m_function = null;
  }

  /** Constants **/
  public void constant(String name) throws RedeclaredIdentException
  {
    if (m_locals.exists(name)) {
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

    m_locals.add(m_name, ident);
    m_name = null;
  }

  /** Variables **/
  public void variable(String name) throws RedeclaredIdentException
  {
    m_index -= YVM.StackValueSize;
    m_locals.add(name, new IdVar(type(), m_index));
  }

  public int countVariables()
  {
    return -(m_index / YVM.StackValueSize);
  }

  /** Functions **/
  public void function(String name) throws RedeclaredIdentException
  {
    m_index = 0;
    m_name = name;
    m_locals.clear();

    if (null != name) {
      m_function = new IdFunct(type());
      m_globals.add(name, m_function);
    }
    else {
      m_name = "main";
      function();
    }
  }

  public void parameter(String name) throws RedeclaredIdentException
  {
    if (null == m_function)
      throw new RuntimeException("No function declared");

    ++m_index;
    m_function.addParameter(type());
    m_locals.add(name, new IdVar(type(), m_index));
  }

  public void function()
  {
    for (Ident ident: m_locals.all()) {
      ident.value(4 + (m_index - ident.value()) * YVM.StackValueSize);
    }

    m_eventManager.emit(Event.Function, m_name);
    m_index = 0;
  }

  public Type functionType()
  {
    return m_function.type();
  }

  /** General purpose **/
  public Type type()
  {
    if (null == m_type)
      throw new RuntimeException("No type");

    return m_type;
  }

  public void type(Type type)
  {
    m_type = type;
  }

  public Ident get(String name) throws UndefinedIdentException
  {
    return m_locals.find(name);
  }

  public String name()
  {
    return m_name;
  }

  public String toString()
  {
    return "-- Global symbols table:\n" + m_globals.toString()
      + "-- Local symbols table:\n" + m_locals.toString();
  }
}
