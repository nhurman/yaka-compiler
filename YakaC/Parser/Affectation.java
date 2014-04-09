package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.AffectationException;
import YakaC.Exception.TypeMismatchException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

/**
 * Handles affectations, ie a = b
 */
public class Affectation
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    Affectation;
  }

  protected Context m_context; /**< Yaka context */
  protected Ident m_ident; /**< Identifier that will store the result */

  /**
   * Constructor
   * @param context Yaka context
   */
  public Affectation(Context context)
  {
    m_context = context;
    m_ident = null;
  }

  /**
   * Sets the identifier that will receive the value
   * @param name Identifier name
   * @throws UndefinedIdentException if identifier is not found
   * @throws AffectationException if the identifier is not a variable
   */
  public void assign(String name) throws UndefinedIdentException, AffectationException
  {
    if (!m_context.locals().exists(name)) {
      m_context.errorBag().add(new UndefinedIdentException(name));
      m_ident = null;
      return;
    }

    m_ident = m_context.locals().find(name);
    if (Ident.Kind.Variable != m_ident.kind()) {
      m_context.errorBag().add(new AffectationException(name, m_ident));
      m_ident = null;
      return;
    }

    m_context.typeChecker().push(m_ident.type());
  }

  /**
   * Sets the value and checks types
   * @throws UndefinedIdentException if identifier is not found
   * @throws AffectationException if the identifier is not a variable
   */
  public void assign() throws UndefinedIdentException, TypeMismatchException
  {
    if (null == m_ident) {
      return;
    }

    m_context.typeChecker().push(Expression.Operator.Assign);
    m_context.typeChecker().check();
    m_context.typeChecker().popType();

    m_context.eventManager().emit(Event.Affectation,
      new Integer(m_ident.value()));
    m_ident = null;
  }
}
