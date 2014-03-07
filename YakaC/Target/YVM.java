package YakaC.Target;

import YakaC.javacc.Yaka;
import YakaC.Event.*;
import YakaC.Event.Event;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident;
import YakaC.Parser.TabIdent;
import YakaC.Exception.UndefinedIdentException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class YVM
{
  public static final int StackValueSize = 2;

  protected PrintWriter m_writer;
  protected boolean m_error;


  public YVM(final Yaka yaka, OutputStream os)
  {
    m_writer = new java.io.PrintWriter(os, true);
    m_error = false;

    final EventManager manager = yaka.eventManager();

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
    }, "YVM");

    manager.register(Event.ExpressionsStart, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("ouvrePrinc " + yaka.tabIdent()
          .count(Ident.Kind.Variable) * StackValueSize);
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
          Ident ident = yaka.tabIdent().find((String)params);
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
        Operator op = (Operator)params;
        if (Operator.Plus == op) {
          m_writer.println("iadd");
        }
        else if (Operator.Minus == op) {
          m_writer.println("isub");
        }
        else if (Operator.Times == op) {
          m_writer.println("imul");
        }
        else if (Operator.Div == op) {
          m_writer.println("idiv");
        }

        else if (Operator.Lower == op) {
          m_writer.println("iinf");
        }
        else if (Operator.Greater == op) {
          m_writer.println("isup");
        }
        else if (Operator.LowerE == op) {
          m_writer.println("iinfegal");
        }
        else if (Operator.GreaterE == op) {
          m_writer.println("isupegal");
        }

        else if (Operator.Equals == op) {
          m_writer.println("iegal");
        }
        else if (Operator.NEquals == op) {
          m_writer.println("idiff");
        }
        else if (Operator.And == op) {
          m_writer.println("iand");
        }
        else if (Operator.Or == op) {
          m_writer.println("ior");
        }

        else if (Operator.Negate == op) {
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
