package Yaka;

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

    public static String[] str = {
      "+", "-", "*", "/",
      "<", ">", "<=", ">=",
      "=", "<>", "ET", "OU"
    };
  };

  ArrayDeque<Operator> m_opStack;
  ArrayDeque<Ident.Type> m_typeStack;

  public Expression()
  {
    m_opStack = new ArrayDeque<Operator>();
    m_typeStack = new ArrayDeque<Ident.Type>();
  }

  public static Ident.Type computeType(Ident.Type t1, Operator op, Ident.Type t2)
  {
    if (t1 != t2) {
      return Ident.Type.Error;
    }

    int o = op.ordinal();

    if (Operator.Plus.ordinal() <= o && o <= Operator.Div.ordinal()) {
      if (Ident.Type.Integer == t1) {
        return Ident.Type.Integer;
      }
    }
    else if (Operator.Lower.ordinal() <= o && o <= Operator.GreaterE.ordinal()) {
      if (Ident.Type.Integer == t1) {
        return Ident.Type.Boolean;
      }
    }
    else if (Operator.Equals.ordinal() <= o && o <= Operator.NEquals.ordinal()) {
      if (Ident.Type.Error != t1) {
        return Ident.Type.Boolean;
      }
    }
    else if (Operator.And.ordinal() <= o && o <= Operator.Or.ordinal()) {
      if (Ident.Type.Boolean == t1) {
        return Ident.Type.Boolean;
      }
    }

    return Ident.Type.Error;
  }

  public void push(Operator op)
  {
    m_opStack.push(op);
  }

  public void push(Ident ident)
  {
    m_typeStack.push(ident.type());
  }

  public void push(Ident.Type type)
  {
    m_typeStack.push(type);
  }

  public void operation() throws TypeMismatchException
  {
    Ident.Type t2 = m_typeStack.pop();
    Ident.Type t1 = m_typeStack.pop();
    Operator op = m_opStack.pop();
    Ident.Type result = computeType(t1, op, t2);

    //if (result == Ident.Type.Error)
    //  throw new TypeMismatchException();

    m_typeStack.push(result);
  }

  public String toString()
  {
    String out = "-- Operators:\n";
    for (Operator op: m_opStack) {
      out += Operator.str[op.ordinal()] + " ";
    }

    out += "\n-- Types:\n";
    for (Ident.Type type: m_typeStack) {
      out += Ident.Type.str[type.ordinal()] + "\n";
    }

    return out;
  }
}
