package YakaC.Target;

import YakaC.javacc.Yaka;
import YakaC.Event.*;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident;
import YakaC.Parser.TabIdent;
import YakaC.Exception.UndefinedIdentException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class YVM
{
  public static final int StackValueSize = 2;

  public static enum Event implements YakaC.Event.Event
  {
    Header,
    StackDefinition,
    IConst,
    ILoad,
    IStore,
    IAdd,
    ISub,
    IMul,
    IDiv,
    IInf,
    IInfEgal,
    ISup,
    ISupEgal,
    IEgal,
    IDiff,
    IDif,
    IAnd,
    IOr,
    INeg,
    ReadInteger,
    WriteBoolean,
    WriteInteger,
    WriteString,
    NewLine,
    Footer;
  }

  protected PrintWriter m_writer;
  protected boolean m_error;

  public YVM(final Yaka yaka, OutputStream os)
  {
    m_writer = new java.io.PrintWriter(os, true);
    m_error = false;

    final EventManager manager = yaka.eventManager();

    manager.register(YakaC.Parser.ErrorBag.Event.Error, new EventHandler() {
      public void execute(Object params) {
        manager.unregister("YVM");
        m_writer.println("Stopping YVM code generation due to error");
      }
    }, "YVM");

    manager.register(Yaka.Event.ProgramStart, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("entete");
        manager.emit(Event.Header, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.InstructionsStart, new EventHandler() {
      public void execute(Object params) {
        int size = yaka.tabIdent().count(Ident.Kind.Variable) * StackValueSize;
        m_writer.println("ouvrePrinc " + size);
        manager.emit(Event.StackDefinition, new Integer(size));
      }
    }, "YVM");

    manager.register(Yaka.Event.Integer, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("iconst " + params);
        manager.emit(Event.IConst, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Affectation.Event.Affectation, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("istore " + params);
        manager.emit(Event.IStore, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.Identifier, new EventHandler() {
      public void execute(Object params) {
        try {
          Ident ident = yaka.tabIdent().find((String)params);
          if (Ident.Kind.Constant == ident.kind()) {
            m_writer.println("iconst " + ident.value());
            manager.emit(Event.IConst, new Integer(ident.value()));
          }
          else if (Ident.Kind.Variable == ident.kind()) {
            m_writer.println("iload " + ident.value());
            manager.emit(Event.ILoad, new Integer(ident.value()));
          }
          else {
            throw new RuntimeException("Unknown identifier kind: " + ident.kind());
          }
        } catch (UndefinedIdentException e) {
          throw new RuntimeException("Undefined ident " + params);
        }
      }
    }, "YVM");

    manager.register(Yaka.Event.Boolean, new EventHandler() {
      public void execute(Object params) {
        Boolean b = (Boolean)params;
        int value = b ? Ident.Boolean.True : Ident.Boolean.False;
        m_writer.println("iconst " + value);
        manager.emit(Event.IConst, new Integer(value));
      }
    }, "YVM");

    manager.register(YakaC.Parser.Expression.Event.Operation, new EventHandler() {
      public void execute(Object params) {
        Operator op = (Operator)params;
        if (Operator.Plus == op) {
          m_writer.println("iadd");
          manager.emit(Event.IAdd);
        }
        else if (Operator.Minus == op) {
          m_writer.println("isub");
          manager.emit(Event.ISub);
        }
        else if (Operator.Times == op) {
          m_writer.println("imul");
          manager.emit(Event.IMul);
        }
        else if (Operator.Div == op) {
          m_writer.println("idiv");
          manager.emit(Event.IDiv);
        }

        else if (Operator.Lower == op) {
          m_writer.println("iinf");
          manager.emit(Event.IInf);
        }
        else if (Operator.Greater == op) {
          m_writer.println("isup");
          manager.emit(Event.ISup);
        }
        else if (Operator.LowerE == op) {
          m_writer.println("iinfegal");
          manager.emit(Event.IInfEgal);
        }
        else if (Operator.GreaterE == op) {
          m_writer.println("isupegal");
          manager.emit(Event.ISupEgal);
        }

        else if (Operator.Equals == op) {
          m_writer.println("iegal");
          manager.emit(Event.IEgal);
        }
        else if (Operator.NEquals == op) {
          m_writer.println("idiff");
          manager.emit(Event.IDiff);
        }
        else if (Operator.And == op) {
          m_writer.println("iand");
          manager.emit(Event.IAnd);
        }
        else if (Operator.Or == op) {
          m_writer.println("ior");
          manager.emit(Event.IOr);
        }

        else if (Operator.Negate == op) {
          m_writer.println("ineg");
          manager.emit(Event.INeg);
        }

        else {
          throw new RuntimeException("Unimplemented operator: " + op);
        }
      }
    }, "YVM");


    manager.register(YakaC.Parser.EntreeSortie.Event.Read, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("lireEnt " + params);
        manager.emit(Event.ReadInteger, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.EntreeSortie.Event.WriteBoolean, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("ecrireBool");
        manager.emit(Event.WriteBoolean);
      }
    }, "YVM");

    manager.register(YakaC.Parser.EntreeSortie.Event.WriteInteger, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("ecrireEnt");
        manager.emit(Event.WriteInteger);
      }
    }, "YVM");

    manager.register(YakaC.Parser.EntreeSortie.Event.WriteString, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("ecrireChaine " + params);
        manager.emit(Event.WriteString, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.EntreeSortie.Event.NewLine, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("aLaLigne");
        manager.emit(Event.NewLine, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.ProgramEnd, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("queue");
        manager.emit(Event.Footer);
      }
    }, "YVM");
  }
}
