package YakaC.Target;

import YakaC.javacc.Yaka;
import YakaC.Event.*;
import YakaC.Parser.Context;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident;
import YakaC.Exception.UndefinedIdentException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Yaka Virtual Machine code generator
 */
public class YVM extends Writer
{
  public static final int StackValueSize = 2; /**< Size of an integer */

  /** Events */
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
    INot,
    INeg,
    ReadInteger,
    WriteBoolean,
    WriteInteger,
    WriteString,
    NewLine,
    Label,
    JumpFalse,
    Jump,
    FunctionDeclaration,
    FunctionEnd,
    ReserveReturn,
    FunctionCall,
    Return,
    Footer;
  }

  /**
   * Constructor
   * @param context Yaka context
   * @param os Output stream
   */
  public YVM(final Context context, OutputStream os)
  {
    super(os);
    final EventManager manager = context.eventManager();

    manager.register(YakaC.Parser.ErrorBag.Event.Error, new EventHandler() {
      public void execute(Object params) {
        manager.unregister("YVM");
        write("Stopping YVM code generation due to error");
      }
    }, "YVM");

    manager.register(Yaka.Event.ProgramStart, new EventHandler() {
      public void execute(Object params) {
        write("entete");
        manager.emit(Event.Header, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.Integer, new EventHandler() {
      public void execute(Object params) {
        write("iconst " + params);
        manager.emit(Event.IConst, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Affectation.Event.Affectation, new EventHandler() {
      public void execute(Object params) {
        write("istore " + params);
        manager.emit(Event.IStore, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.Identifier, new EventHandler() {
      public void execute(Object params) {
        try {
          Ident ident = context.locals().find((String)params);
          if (Ident.Kind.Constant == ident.kind()) {
            write("iconst " + ident.value());
            manager.emit(Event.IConst, new Integer(ident.value()));
          }
          else if (Ident.Kind.Variable == ident.kind()) {
            write("iload " + ident.value());
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
        write("iconst " + value);
        manager.emit(Event.IConst, new Integer(value));
      }
    }, "YVM");

    manager.register(YakaC.Parser.Expression.Event.Operation, new EventHandler() {
      public void execute(Object params) {
        Operator op = (Operator)params;
        if (Operator.Plus == op) {
          write("iadd");
          manager.emit(Event.IAdd);
        }
        else if (Operator.Minus == op) {
          write("isub");
          manager.emit(Event.ISub);
        }
        else if (Operator.Times == op) {
          write("imul");
          manager.emit(Event.IMul);
        }
        else if (Operator.Div == op) {
          write("idiv");
          manager.emit(Event.IDiv);
        }

        else if (Operator.Lower == op) {
          write("iinf");
          manager.emit(Event.IInf);
        }
        else if (Operator.Greater == op) {
          write("isup");
          manager.emit(Event.ISup);
        }
        else if (Operator.LowerE == op) {
          write("iinfegal");
          manager.emit(Event.IInfEgal);
        }
        else if (Operator.GreaterE == op) {
          write("isupegal");
          manager.emit(Event.ISupEgal);
        }

        else if (Operator.Equals == op) {
          write("iegal");
          manager.emit(Event.IEgal);
        }
        else if (Operator.NEquals == op) {
          write("idiff");
          manager.emit(Event.IDiff);
        }
        else if (Operator.And == op) {
          write("iand");
          manager.emit(Event.IAnd);
        }
        else if (Operator.Or == op) {
          write("ior");
          manager.emit(Event.IOr);
        }

        else if (Operator.Not == op) {
          write("inot");
          manager.emit(Event.INot);
        }
        else if (Operator.Negate == op) {
          write("ineg");
          manager.emit(Event.INeg);
        }

        else {
          throw new RuntimeException("Unimplemented operator: " + op);
        }
      }
    }, "YVM");


    manager.register(YakaC.Parser.IO.Event.Read, new EventHandler() {
      public void execute(Object params) {
        write("lireEnt " + params);
        manager.emit(Event.ReadInteger, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.IO.Event.WriteBoolean, new EventHandler() {
      public void execute(Object params) {
        write("ecrireBool");
        manager.emit(Event.WriteBoolean);
      }
    }, "YVM");

    manager.register(YakaC.Parser.IO.Event.WriteInteger, new EventHandler() {
      public void execute(Object params) {
        write("ecrireEnt");
        manager.emit(Event.WriteInteger);
      }
    }, "YVM");

    manager.register(YakaC.Parser.IO.Event.WriteString, new EventHandler() {
      public void execute(Object params) {
        write("ecrireChaine " + params);
        manager.emit(Event.WriteString, params);
      }
    }, "YVM");

    manager.register(YakaC.Parser.IO.Event.NewLine, new EventHandler() {
      public void execute(Object params) {
        write("aLaLigne");
        manager.emit(Event.NewLine, params);
      }
    }, "YVM");

    // Iteration
    manager.register(YakaC.Parser.Iteration.Event.BeginFor, new EventHandler() {
      public void execute(Object params) {
        String label = "FAIRE" + params;
        write(label + ":");
        manager.emit(Event.Label, label);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Iteration.Event.Condition, new EventHandler() {
      public void execute(Object params) {
        String label = "FAIT" + params;
        write("iffaux " + label);
        manager.emit(Event.JumpFalse, label);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Iteration.Event.EndFor, new EventHandler() {
      public void execute(Object params) {
        String label = "FAIRE" + params;
        write("goto " + label);
        manager.emit(Event.Jump, label);

        label = "FAIT" + params;
        write(label + ":");
        manager.emit(Event.Label, label);
      }
    }, "YVM");

    // Branching
    manager.register(YakaC.Parser.Branching.Event.Condition, new EventHandler() {
      public void execute(Object params) {
        String label = "SINON" + params;
        write("iffaux " + label);
        manager.emit(Event.JumpFalse, label);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Branching.Event.BeginElse, new EventHandler() {
      public void execute(Object params) {
        String label = "FSI" + params;
        write("goto " + label);
        manager.emit(Event.Jump, label);

        label = "SINON" + params;
        write(label + ":");
        manager.emit(Event.Label, label);
      }
    }, "YVM");

    manager.register(YakaC.Parser.Branching.Event.EndIf, new EventHandler() {
      public void execute(Object params) {
        String label = "FSI" + params;
        write(label + ":");
        manager.emit(Event.Label, label);
      }
    }, "YVM");

    // Function

    manager.register(Yaka.Event.InstructionsStart, new EventHandler() {
      public void execute(Object params) {
        int size = context.locals().count(Ident.Kind.Variable, -1) * StackValueSize;
        write("ouvbloc " + size);
        manager.emit(Event.StackDefinition, new Integer(size));
      }
    }, "YVM");

    manager.register(YakaC.Parser.Declaration.Event.Function, new EventHandler() {
      public void execute(Object params) {
        write(params + ":");
        manager.emit(Event.FunctionDeclaration, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.Return, new EventHandler() {
      public void execute(Object params) {
        int ret = (2 + context.locals().count(Ident.Kind.Variable, 1)) * StackValueSize;
        write("ireturn " + ret);
        manager.emit(Event.Return, new Integer(ret));
      }
    }, "YVM");

    manager.register(Yaka.Event.FunctionEnd, new EventHandler() {
      public void execute(Object params) {
        int nbParams = (context.locals().count(Ident.Kind.Variable, 1)) * StackValueSize;
        write("fermebloc " + nbParams);
        manager.emit(Event.FunctionEnd, new Integer(nbParams));
      }
    }, "YVM");

    manager.register(YakaC.Parser.Expression.Event.FunctionPreCall, new EventHandler() {
      public void execute(Object params) {
        write("reserveRetour");
        manager.emit(Event.ReserveReturn);
      }
    }, "YVM");

    manager.register(Yaka.Event.FunctionCall, new EventHandler() {
      public void execute(Object params) {
        write("call " + params);
        manager.emit(Event.FunctionCall, params);
      }
    }, "YVM");

    manager.register(Yaka.Event.ProgramEnd, new EventHandler() {
      public void execute(Object params) {
        write("queue");
        manager.emit(Event.Footer);
      }
    }, "YVM");

  }

  /**
   * Output a string
   * @param str String
   */
  protected void write(String str)
  {
    super.write("; " + str);
  }

  /**
   * Output a string with an indentation level
   * @param indent Indentation level
   * @param str String
   */
  protected void write(int indent, String str)
  {
    super.write(indent, "; " + str);
  }
}
