package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Parser.Ident.Kind;
import YakaC.Event.*;
import YakaC.Exception.TypeMismatchException;
import YakaC.Exception.CallException;
import YakaC.Exception.SignatureException;
import YakaC.Exception.MainReturnException;
import YakaC.Exception.ReturnTypeException;
import java.util.ArrayDeque;

public class Expression
{
  public static enum Event implements YakaC.Event.Event
  {
    Operation,
    FunctionPreCall;
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

    Not,
    Negate;

    protected boolean m_unary = false;

    static {
      Not.m_unary = true;
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
  protected TabIdent m_globals;
  protected Declaration m_declaration;
  protected ArrayDeque<Ident> m_functionStack;
  protected ArrayDeque<IdFunct> m_signatureStack;

  public Expression(ErrorBag errors, EventManager eventManager, TypeChecker typeChecker, TabIdent globals, Declaration declaration)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_typeChecker = typeChecker;
    m_globals = globals;
    m_declaration = declaration;
    m_functionStack = new ArrayDeque<Ident>();
    m_signatureStack = new ArrayDeque<IdFunct>();
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

  public void call(Ident function)
  {
    if (Kind.Undefined == function.kind()) {
      function = new IdFunct(Type.Error);
    }
    else if (Kind.Function != function.kind()) {
      m_errors.add(new CallException(null, function));
      return;
    }

    m_functionStack.push(function);
    m_signatureStack.push(new IdFunct());
    m_eventManager.emit(Event.FunctionPreCall);
  }

  public void call()
  {
    if (m_functionStack.isEmpty() || m_signatureStack.isEmpty()) {
      m_typeChecker.push(Type.Error);
      return;
    }

    IdFunct function = (IdFunct)m_functionStack.pop();
    IdFunct signature = m_signatureStack.pop();

    if (Type.Error != function.type() && !function.equals(signature)) {
      m_errors.add(new SignatureException(function, signature));
    }

    m_typeChecker.push(function.type());
  }

  public void functionParameter()
  {
    if (m_signatureStack.isEmpty()) {
      m_typeChecker.popType();
      return;
    }

    m_signatureStack.peek().addParameter(m_typeChecker.popType());
  }

  public void returnValue()
  {
    if ("main".equals(m_declaration.name())) {
      m_errors.add(new MainReturnException());
    }

    Type t = m_typeChecker.popType();
    if (m_declaration.functionType() != t) {
      m_errors.add(new ReturnTypeException(m_declaration.functionType(), t));
    }
  }

  public String toString()
  {
    return m_typeChecker.toString();
  }
}
