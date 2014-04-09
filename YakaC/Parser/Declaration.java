package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Ident.Boolean;
import YakaC.Exception.RedeclaredIdentException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

/**
 * Handles variable/constant/function declarations
 */
public class Declaration
{

  protected Context m_context; /**< Yaka context */
  protected int m_index; /**< Index for variable positions in the stack */
  protected String m_name; /**< Last identifier name */
  protected Type m_type; /**< Last identifier type */
  protected IdFunct m_function; /**< Current function */

  /**
   * Constructor
   * @param context Yaka context
   */
  public Declaration(Context context)
  {
    m_context = context;
    m_index = 0;
    m_name = null;
    m_type = null;
    m_function = null;
  }

  /**
   * Constant declaration
   * @param name Constant name
   */
  public void constant(String name) throws RedeclaredIdentException
  {
    if (m_context.locals().exists(name)) {
      m_context.errorBag().add(new RedeclaredIdentException(name));
      name = null;
    }

    m_name = name;
  }

  /**
   * Set the constant's value
   * @param type Constant type
   * @param value Constant value
   */
  public void constant(Type type, int value) throws RedeclaredIdentException
  {
    add(new IdConst(type, value));
  }

  /**
   * Set the constant's value
   * @param type Constant type
   * @param b Constant value
   */
  public void constant(Type type, boolean b) throws RedeclaredIdentException
  {
    add(new IdConst(type, b ? Boolean.True : Boolean.False));
  }

  /**
   * Copy another constant
   * @param ident Other constant
   */
  public void constant(Ident ident) throws RedeclaredIdentException
  {
    add(new IdConst(ident.type(), ident.value()));
  }

  /**
   * Add the current constant to the local identifiers table
   * @param ident Constant
   */
  private void add(IdConst ident) throws RedeclaredIdentException
  {
    if (null == m_name)
      return;

    m_context.locals().add(m_name, ident);
    m_name = null;
  }

  /**
   * Declare a variable
   * @param name Variabe name
   */
  public void variable(String name) throws RedeclaredIdentException
  {
    m_index -= YVM.StackValueSize;
    m_context.locals().add(name, new IdVar(type(), m_index));
  }

  /**
   * Count the amount of declared variables
   * @return Variables count
   */
  public int countVariables()
  {
    return -(m_index / YVM.StackValueSize);
  }

  /**
   * Declare a function
   * @param name Function name
   */
  public void function(String name) throws RedeclaredIdentException
  {
    m_index = 0;
    m_name = name;
    m_context.locals().clear();

    if (null != name) {
      m_function = new IdFunct(type());
      m_context.globals().add(name, m_function);
    }
    else {
      m_name = "main";
      function();
    }
  }

  /**
   * Add a parameter to the function declaration
   * @param name Parameter name
   */
  public void parameter(String name) throws RedeclaredIdentException
  {
    if (null == m_function)
      throw new RuntimeException("No function declared");

    ++m_index;
    m_function.addParameter(type());
    m_context.locals().add(name, new IdVar(type(), m_index));
  }

  /**
   * End of function prototype
   */
  public void function()
  {
    for (Ident ident: m_context.locals().all()) {
      ident.value(4 + (m_index - ident.value()) * YVM.StackValueSize);
    }

    m_index = 0;
  }

  /**
   * Get the current function's return type
   * @return Function type
   */
  public Type functionType()
  {
    return m_function.type();
  }

  /**
   * Get the last declared type
   * @return Type
   */
  public Type type()
  {
    if (null == m_type)
      throw new RuntimeException("No type");

    return m_type;
  }

  /**
   * Set the next variable's / function's type
   * @param type Type
   */
  public void type(Type type)
  {
    m_type = type;
  }

  /**
   * Retrieve a local identifier
   * @return Identifier
   */
  public Ident get(String name) throws UndefinedIdentException
  {
    return m_context.locals().find(name);
  }

  /**
   * Get the last declared identifier name
   * @return Identifier name
   */
  public String name()
  {
    return m_name;
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    return "-- Global symbols table:\n" + m_context.globals().toString()
      + "-- Local symbols table:\n" + m_context.locals().toString();
  }
}
