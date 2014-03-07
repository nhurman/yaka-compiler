package YakaC;

import YakaC.Event.*;
import YakaC.Event.Event;
import YakaC.Exception.UndefinedIdentException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class YVM
{
  public static final int StackValueSize = 2;

  protected PrintWriter m_writer;
  protected TabIdent m_tabIdent;
  protected boolean m_error;

  public YVM(OutputStream os, final EventManager manager, TabIdent tabIdent)
  {
    m_writer = new java.io.PrintWriter(os, true);
    m_tabIdent = tabIdent;
    m_error = false;

    manager.register(Event.Error, new EventHandler() {
      public void execute(Object params) {
        manager.unregister("YVM");
        m_writer.println("Stopping YVM code generation due to error");
      }
    }, "YVM");

    manager.register(Event.ProgramStart, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("entete");
      }
    });

    manager.register(Event.ExpressionsStart, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("ouvrePrinc " + m_tabIdent.count(Ident.Kind.Variable) * StackValueSize);
      }
    }, "YVM");

    manager.register(Event.Integer, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("iconst " + params);
      }
    }, "YVM");

    manager.register(Event.Identifier, new EventHandler() {
      public void execute(Object params) {
        try {
          Ident ident = m_tabIdent.find((String)params);
          if (Ident.Kind.Constant == ident.kind()) {
            m_writer.println("iconst " + ident.value());
          }
          else if (Ident.Kind.Variable == ident.kind()) {
            m_writer.println("iload " + ident.value());
          }
          else {
            throw new RuntimeException("Unknown identifier kind: " + ident.kind());
          }
        } catch (UndefinedIdentException e) {
          throw new RuntimeException("Undefined ident " + params);
        }
      }
    }, "YVM");

    manager.register(Event.Boolean, new EventHandler() {
      public void execute(Object params) {
        Boolean b = (Boolean)params;
        int value = b ? Ident.Boolean.True : Ident.Boolean.False;
        m_writer.println("iconst " + value);
      }
    }, "YVM");

    manager.register(Event.Operation, new EventHandler() {
      public void execute(Object params) {
        Expression.Operator op = (Expression.Operator)params;
        if (Expression.Operator.Plus == op) {
          m_writer.println("iadd");
        }
        else if (Expression.Operator.Minus == op) {
          m_writer.println("isub");
        }
        else if (Expression.Operator.Times == op) {
          m_writer.println("imul");
        }
        else if (Expression.Operator.Div == op) {
          m_writer.println("idiv");
        }

        else if (Expression.Operator.Lower == op) {
          m_writer.println("iinf");
        }
        else if (Expression.Operator.Greater == op) {
          m_writer.println("isup");
        }
        else if (Expression.Operator.LowerE == op) {
          m_writer.println("iinfegal");
        }
        else if (Expression.Operator.GreaterE == op) {
          m_writer.println("isupegal");
        }

        else if (Expression.Operator.Equals == op) {
          m_writer.println("iegal");
        }
        else if (Expression.Operator.NEquals == op) {
          m_writer.println("idiff");
        }
        else if (Expression.Operator.And == op) {
          m_writer.println("iand");
        }
        else if (Expression.Operator.Or == op) {
          m_writer.println("ior");
        }

        else if (Expression.Operator.Neg == op) {
          m_writer.println("ineg");
        }

        else {
          throw new RuntimeException("Unimplemented operator: " + op);
        }
      }
    }, "YVM");

    manager.register(Event.ProgramEnd, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("queue");
      }
    }, "YVM");
  }
}
