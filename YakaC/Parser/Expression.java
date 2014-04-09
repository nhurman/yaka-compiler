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

/**
 * Handles expressions, ie (1 + a) * b to check type compatibility
 */
public class Expression
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    Operation,
    FunctionPreCall;
  }

  /**
   * Available operators
   */
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

    /**
     * Set to true if the operator only takes one operand
     */
    protected boolean m_unary = false;

    static {
      Not.m_unary = true;
      Negate.m_unary = true;
    }

    /**
     * Returns true if the operator is unary
     * @return unary?
     */
    public boolean unary()
    {
      return m_unary;
    }
  };

  protected Context m_context; /**< Yaka context */
  protected ArrayDeque<Ident> m_functionStack; /**< Function call stack */
  protected ArrayDeque<IdFunct> m_signatureStack; /**< Function signature stack */

  /**
   * Constructor
   * @param context Yaka context
   */
  public Expression(Context context)
  {
    m_context = context;
    m_functionStack = new ArrayDeque<Ident>();
    m_signatureStack = new ArrayDeque<IdFunct>();
  }

  /**
   * Push an operator on the stack
   * @param op Operator
   */
  public void push(Operator op)
  {
    m_context.typeChecker().push(op);
  }

  /**
   * Push a type on the stack
   * @param type Type
   */
  public void push(Type type)
  {
    m_context.typeChecker().push(type);
  }

  /**
   * Push an identifier's type on the stack
   * @param ident Identifier
   */
  public void push(Ident ident)
  {
    push(ident.type());
  }

  /**
   * Compute an operation
   * @throws TypeMismatchException if types are incompatible
   */
  public void operation() throws TypeMismatchException
  {
    Operator operator = m_context.typeChecker().check();
    m_context.eventManager().emit(Event.Operation, operator);
  }

  /**
   * Function pre-call
   * @param function Function
   */
  public void call(Ident function)
  {
    if (Kind.Undefined == function.kind()) {
      function = new IdFunct(Type.Error);
    }
    else if (Kind.Function != function.kind()) {
      m_context.errorBag().add(new CallException(null, function));
      return;
    }

    m_functionStack.push(function);
    m_signatureStack.push(new IdFunct());
    m_context.eventManager().emit(Event.FunctionPreCall);
  }

  /**
   * End of function arguments
   */
  public void call()
  {
    if (m_functionStack.isEmpty() || m_signatureStack.isEmpty()) {
      m_context.typeChecker().push(Type.Error);
      return;
    }

    IdFunct function = (IdFunct)m_functionStack.pop();
    IdFunct signature = m_signatureStack.pop();

    if (Type.Error != function.type() && !function.equals(signature)) {
      m_context.errorBag().add(new SignatureException(function, signature));
    }

    m_context.typeChecker().push(function.type());
  }

  /**
   * Add a function parameter
   */
  public void functionParameter()
  {
    if (m_signatureStack.isEmpty()) {
      m_context.typeChecker().popType();
      return;
    }

    m_signatureStack.peek().addParameter(m_context.typeChecker().popType());
  }

  /**
   * Return statement
   */
  public void returnValue()
  {
    if ("main".equals(m_context.declaration().name())) {
      m_context.errorBag().add(new MainReturnException());
    }

    Type t = m_context.typeChecker().popType();
    if (m_context.declaration().functionType() != t) {
      m_context.errorBag().add(new ReturnTypeException(m_context.declaration().functionType(), t));
    }
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
  public String toString()
  {
    return m_context.typeChecker().toString();
  }
}
