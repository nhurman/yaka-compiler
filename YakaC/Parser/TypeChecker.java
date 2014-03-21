package YakaC.Parser;

import java.util.ArrayDeque;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident.Type;
import YakaC.Exception.TypeMismatchException;

public class TypeChecker
{
  protected ErrorBag m_errors;
  protected ArrayDeque<Operator> m_opStack;
  protected ArrayDeque<Type> m_typeStack;

  public TypeChecker(ErrorBag errors)
  {
    m_errors = errors;
    m_opStack = new ArrayDeque<Operator>();
    m_typeStack = new ArrayDeque<Type>();
  }

  protected static Type computeType(Operator op, Type t1, Type t2)
  {
    if (op.unary()) {
      if (Operator.Not == op) {
        if (Type.Boolean == t1) {
          return Type.Boolean;
        }
      }
      else if (Operator.Negate == op) {
        if (Type.Integer == t1) {
          return Type.Integer;
        }
      }
    }
    else if (t1 == t2) {
      int o = op.ordinal();

      if (Operator.Assign == op) {
        return t1;
      }
      else if (Operator.Plus.ordinal() <= o && o <= Operator.Div.ordinal()) {
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

  public Operator check()
  {
    Type t1, t2 = null, result;
    Operator op = m_opStack.pop();

    if (!op.unary()) {
      t2 = m_typeStack.pop();
    }

    t1 = m_typeStack.pop();
    result = computeType(op, t1, t2);
    m_typeStack.push(result);

    if (Type.Error == result && Type.Error != t1 && Type.Error != t2) {
      m_errors.add(new TypeMismatchException(op, t1, t2));
    }

    return op;
  }

  public void push(Type t)
  {
    m_typeStack.push(t);
  }

  public void push(Operator op)
  {
    m_opStack.push(op);
  }

  public Type lastType()
  {
    return m_typeStack.peek();
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
