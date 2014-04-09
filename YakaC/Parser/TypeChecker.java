package YakaC.Parser;

import java.util.ArrayDeque;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident.Type;
import YakaC.Exception.TypeMismatchException;

/**
 * Check type compatibility
 */
public class TypeChecker
{
  protected Context m_context; /**< Yaka context */
  protected ArrayDeque<Operator> m_opStack; /**< Operators stack */
  protected ArrayDeque<Type> m_typeStack; /**< Types stack */

  /**
   * Constructor
   * @param context Yaka context
   */
  public TypeChecker(Context context)
  {
    m_context = context;
    m_opStack = new ArrayDeque<Operator>();
    m_typeStack = new ArrayDeque<Type>();
  }

  /**
   * Compute the result type of an operation
   * @param op Operator
   * @param t1 Type of first operand
   * @param t2 Type of second operand, ignored if op is unary
   * @return Result type
   */
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

  /**
   * Check the last operation is valid
   * @return Operator
   */
  public Operator check()
  {
    Type t1, t2 = null, result;
    Operator op = m_opStack.pop();

    if (!op.unary()) {
      t2 = popType();
    }

    t1 = popType();
    result = computeType(op, t1, t2);
    push(result);

    if (Type.Error == result && Type.Error != t1 && Type.Error != t2) {
      m_context.errorBag().add(new TypeMismatchException(op, t1, t2));
    }

    return op;
  }

  /**
   * Add a type
   * @param t Type
   */
  public void push(Type t)
  {
    m_typeStack.push(t);
  }

  /**
   * Add an operator
   * @param op Operator
   */
  public void push(Operator op)
  {
    m_opStack.push(op);
  }

  /**
   * Get the most recent type
   * @return Type
   */
  public Type peekType()
  {
    return m_typeStack.peek();
  }

  /**
   * Get and delete the most recent type
   * @return Type
   */
  public Type popType()
  {
    return m_typeStack.pop();
  }

  /**
   * Debug-purpose string object representation
   * @return String
   */
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
