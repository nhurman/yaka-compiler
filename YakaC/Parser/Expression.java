package YakaC.Parser;

import YakaC.Parser.Ident.Type;
import YakaC.Event.*;
import YakaC.Exception.TypeMismatchException;
import java.util.ArrayDeque;

public class Expression
{
  public static enum Operator {
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

  ErrorBag m_errors;
  EventManager m_eventManager;
  ArrayDeque<Operator> m_opStack;
  ArrayDeque<Type> m_typeStack;

  public Expression(ErrorBag errors, EventManager eventManager)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_opStack = new ArrayDeque<Operator>();
    m_typeStack = new ArrayDeque<Type>();
  }

  protected static Type computeType(Operator op, Type t1, Type t2)
  {
    if (op.unary()) {
      if (Operator.Negate == op) {
        return t1;
      }
    }
    else {
      int o = op.ordinal();

      if (Operator.Plus.ordinal() <= o && o <= Operator.Div.ordinal()) {
        if (Type.Integer == t1) {
          return Type.Integer;
        }
      }
      else if (Operator.Lower.ordinal() <= o && o <= Operator.GreaterE.ordinal()) {
        if (Type.Integer == t1) {
          return Type.Boolean;
        }
      }
      else if (Operator.Equals.ordinal() <= o && o <= Operator.NEquals.ordinal()) {
        if (Type.Error != t1) {
          return Type.Boolean;
        }
      }
      else if (Operator.And.ordinal() <= o && o <= Operator.Or.ordinal()) {
        return t1;
      }
    }

    return Type.Error;
  }

  protected void checkType(Operator op)
  {
    Type t1, t2 = null, result;

    if (!op.unary()) {
      t2 = m_typeStack.pop();
    }

    t1 = m_typeStack.pop();
    result = computeType(op, t1, t2);
    m_typeStack.push(result);

    if (Type.Error == result && Type.Error != t1 && Type.Error != t2) {
      m_errors.add(new TypeMismatchException(op, t1, t2));
    }
  }

  public void push(Operator op)
  {
    m_opStack.push(op);
  }

  public void push(Ident ident)
  {
    m_typeStack.push(ident.type());
  }

  public void push(Type type)
  {
    m_typeStack.push(type);
  }

  public void operation() throws TypeMismatchException
  {
    Operator operator = m_opStack.pop();
    checkType(operator);
    m_eventManager.emit(Event.Operation, operator);
  }

  public String toString()
  {
    String out = "-- Operators:\n";
    for (Operator op: m_opStack) {
      out += op + " ";
    }

    out += "\n-- Types:\n";
    for (Type type: m_typeStack) {
      out += type + "\n";
    }

    return out;
  }
}
