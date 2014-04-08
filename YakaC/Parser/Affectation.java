package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.AffectationException;
import YakaC.Exception.TypeMismatchException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

public class Affectation
{
  public static enum Event implements YakaC.Event.Event
  {
    Affectation;
  }

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TabIdent m_tabIdent;
  protected TypeChecker m_typeChecker;
  protected String m_name;

  public Affectation(ErrorBag errors, EventManager eventManager, TabIdent tabIdent, TypeChecker typeChecker)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_tabIdent = tabIdent;
    m_typeChecker = typeChecker;
    m_name = null;
  }

  public void assign(String name) throws UndefinedIdentException, AffectationException
  {
    if (!m_tabIdent.exists(name)) {
      m_errors.add(new UndefinedIdentException(name));
      m_name = null;
      return;
    }

    Ident ident = m_tabIdent.find(name);
    if (Ident.Kind.Variable != ident.kind()) {
      m_errors.add(new AffectationException(name, ident));
      m_name = null;
      return;
    }

    m_name = name;
    m_typeChecker.push(m_tabIdent.find(name).type());
  }

  public void assign() throws UndefinedIdentException, TypeMismatchException
  {
    if (null == m_name) {
      return;
    }

    m_typeChecker.push(Expression.Operator.Assign);
    m_typeChecker.check();
    m_typeChecker.popType();

    m_eventManager.emit(Event.Affectation,
      new Integer(m_tabIdent.find(m_name).value()));
    m_name = null;
  }
}
