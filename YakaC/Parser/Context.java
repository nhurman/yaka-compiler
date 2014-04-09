package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.javacc.Yaka;

/**
 * Yaka compiler context, contains a bunch of instances used throughout the different classes
 */
public class Context
{
  protected Yaka         m_yaka;         /**< Parser */
  protected EventManager m_eventManager; /**< Event manager */
  protected ErrorBag     m_errors;       /**< Error list */
  protected TabIdent     m_globals;      /**< Global identifiers table */
  protected TabIdent     m_locals;       /**< Local identifiers table */
  protected TypeChecker  m_typeChecker;  /**< Type checker */
  protected Declaration  m_declaration;  /**< Declarations */
  protected Expression   m_expression;   /**< Expressions */
  protected Affectation  m_affectation;  /**< Affectations */
  protected IO           m_io;           /**< I/O */
  protected Iteration    m_iteration;    /**< Iterations */
  protected Branching    m_branching;    /**< Branchings */

  /**
   * Creates a context
   * @param yaka Yaka parser
   */
  public Context(YakaC.javacc.Yaka yaka)
  {
    m_yaka         = yaka;
    m_eventManager = new EventManager();
    m_errors       = new ErrorBag(this, false);
    m_globals      = new TabIdent(this, 10);
    m_locals       = new TabIdent(this, 10);
    m_typeChecker  = new TypeChecker(this);
    m_declaration  = new Declaration(this);
    m_expression   = new Expression(this);
    m_affectation  = new Affectation(this);
    m_io           = new IO(this);
    m_iteration    = new Iteration(this);
    m_branching    = new Branching(this);
  }

  /**
   * Fetch the yaka parser
   * @return Yaka
   */
  public Yaka yaka()
  {
    return m_yaka;
  }

  /**
   * Fetch the event manager
   * @return EventManager
   */
  public EventManager eventManager()
  {
    return m_eventManager;
  }

  /**
   * Fetch the error list
   * @return ErrorBag
   */
  public ErrorBag errorBag()
  {
    return m_errors;
  }

  /**
   * Fetch the global identifiers table
   * @return Globals
   */
  public TabIdent globals()
  {
    return m_globals;
  }

  /**
   * Fetch the local identifiers table
   * @return Locals
   */
  public TabIdent locals()
  {
    return m_locals;
  }

  /**
   * Fetch the type checker
   * @return TypeChecker
   */
  public TypeChecker typeChecker()
  {
    return m_typeChecker;
  }

  /**
   * Fetch the declarations handler
   * @return Declaration
   */
  public Declaration declaration()
  {
    return m_declaration;
  }

  /**
   * Fetch the expressions handler
   * @return Expression
   */
  public Expression expression()
  {
    return m_expression;
  }

  /**
   * Fetch the affectations handler
   * @return Affectation
   */
  public Affectation affectation()
  {
    return m_affectation;
  }

  /**
   * Fetch the I/O handler
   * @return IO
   */
  public IO io()
  {
    return m_io;
  }

  /**
   * Fetch the iterations handler
   * @return Iteration
   */
  public Iteration iteration()
  {
    return m_iteration;
  }

  /**
   * Fetch the branching handler
   * @return Branching
   */
  public Branching branching()
  {
    return m_branching;
  }
}
