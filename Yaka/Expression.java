package Yaka;

import Yaka.Ident.Type;
import Yaka.Exception.TypeMismatchException;
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
    Or;

    public static final String[] str = {
      "+", "-", "*", "/",
      "<", ">", "<=", ">=",
      "=", "<>", "ET", "OU"
    };
  };

  ErrorBag m_errors;
  ArrayDeque<Operator> m_opStack;
  ArrayDeque<Type> m_typeStack;

  public Expression(ErrorBag errors)
  {
    m_errors = errors;
    m_opStack = new ArrayDeque<Operator>();
    m_typeStack = new ArrayDeque<Type>();
  }

  public static Type computeType(Type t1, Operator op, Type t2)
  {
    if (t1 != t2) {
      return Type.Error;
    }

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
      if (Type.Boolean == t1) {
        return Type.Boolean;
      }
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
    Operator op = m_opStack.pop();
    Type result = computeType(t1, op, t2);

    if (result == Type.Error
     && Type.Error != t1
     && Type.Error != t2)
      m_errors.add(new TypeMismatchException(t1, op, t2));

    m_typeStack.push(result);
  }

  public String toString()
  {
    String out = "-- Operators:\n";
    for (Operator op: m_opStack) {
      out += Operator.str[op.ordinal()] + " ";
    }

    out += "\n-- Types:\n";
    for (Type type: m_typeStack) {
      out += Type.str[type.ordinal()] + "\n";
    }

    return out;
  }
}
