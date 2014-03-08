package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Event.*;
import YakaC.Exception.TypeMismatchException;
import java.util.ArrayDeque;

public class Expression
{
  public static enum Event implements YakaC.Event.Event
  {
    Operation;
  }

  public static enum Operator {
    Assign,

    Plus,
    Minus,
    Times,
    Div,

    Lower,
    Greater,
    LowerE,
    GreaterE,

    Equals,
    NEquals,
    And,
    Or,

    Negate;

    protected boolean m_unary = false;

    static {
      Negate.m_unary = true;
    }

    public boolean unary()
    {
      return m_unary;
    }
  };

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TypeChecker m_typeChecker;

  public Expression(ErrorBag errors, EventManager eventManager, TypeChecker typeChecker)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_typeChecker = typeChecker;
  }

  public void push(Operator op)
  {
    m_typeChecker.push(op);
  }

  public void push(Type type)
  {
    m_typeChecker.push(type);
  }

  public void push(Ident ident)
  {
    push(ident.type());
  }

  public void operation() throws TypeMismatchException
  {
    Operator operator = m_typeChecker.check();
    m_eventManager.emit(Event.Operation, operator);
  }

  public String toString()
  {
    return m_typeChecker.toString();
  }
}
