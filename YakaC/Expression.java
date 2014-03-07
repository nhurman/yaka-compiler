package YakaC;

import YakaC.Ident.Type;
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

    Neg;
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

  public static Type computeType(Operation op)
  {
    if (op.t1 != op.t2) {
      return Type.Error;
    }

    int o = op.op.ordinal();

    if (Operator.Plus.ordinal() <= o && o <= Operator.Div.ordinal()) {
      if (Type.Integer == op.t1) {
        return Type.Integer;
      }
    }
    else if (Operator.Lower.ordinal() <= o && o <= Operator.GreaterE.ordinal()) {
      if (Type.Integer == op.t1) {
        return Type.Boolean;
      }
    }
    else if (Operator.Equals.ordinal() <= o && o <= Operator.NEquals.ordinal()) {
      if (Type.Error != op.t1) {
        return Type.Boolean;
      }
    }
    else if (Operator.And.ordinal() <= o && o <= Operator.Or.ordinal()) {
      return op.t1;
    }

    return Type.Error;
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
    Type t2 = m_typeStack.pop();
    Type t1 = m_typeStack.pop();
    Operator operator = m_opStack.pop();
    Operation op = new Operation(t1, operator, t2);

    Type result = computeType(op);

    if (result == Type.Error
     && Type.Error != t1
     && Type.Error != t2)
      m_errors.add(new TypeMismatchException(op));

    m_typeStack.push(result);
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
