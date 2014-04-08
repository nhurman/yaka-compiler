package YakaC.Target;

import YakaC.javacc.Yaka;
import YakaC.Event.*;
import YakaC.Target.YVM;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Tasm extends Writer
{
  public static final int StackValueSize = 2;

  protected int m_strIndex;

  public Tasm(final Yaka yaka, OutputStream os)
  {
    super(yaka, os);
    m_strIndex = 0;
    final EventManager manager = yaka.eventManager();

    manager.register(YakaC.Parser.ErrorBag.Event.Error, new EventHandler() {
      public void execute(Object params) {
        manager.unregister("ASM");
        write("Stopping ASM code generation due to error");
      }
    }, "ASM");

    manager.register(YVM.Event.Header, new EventHandler() {
      public void execute(Object params) {
        String[] functions = {
          "lirent",
          "ecrch",
          "ecrent",
          "ecrbool",
          "ligsuiv"
        };

        for (String function: functions) {
          write("extrn " + function + ":proc");
        }

        write(".model SMALL");
        write(".586");
        write(".CODE");
      }
    }, "ASM");

    manager.register(YVM.Event.StackDefinition, new EventHandler() {
      public void execute(Object params) {
        write(1, "enter " + params + ", 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IConst, new EventHandler() {
      public void execute(Object params) {
        write(1, "push " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.ILoad, new EventHandler() {
      public void execute(Object params) {
        write(1, "push word ptr [bp" + offset((int)params) + "]");
      }
    }, "ASM");

    manager.register(YVM.Event.IStore, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop ax");
        write(1, "mov word ptr [bp" + offset((int)params) + "], ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IAdd, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "add ax, bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.ISub, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "sub ax, bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IMul, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "imul bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IDiv, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cwd");
        write(1, "idiv bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IInf, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "jge $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IInfEgal, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "jg $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.ISup, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "jle $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.ISupEgal, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "jl $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IEgal, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "jne $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IDiff, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "cmp ax, bx");
        write(1, "je $+6");
        write(1, "push -1");
        write(1, "jmp $+4");
        write(1, "push 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IAnd, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "and ax, bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IOr, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop bx");
        write(1, "pop ax");
        write(1, "or ax, bx");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.INot, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop ax");
        write(1, "not ax");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.INeg, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop ax");
        write(1, "neg ax");
        write(1, "push ax");
      }
    }, "ASM");

    manager.register(YVM.Event.ReadInteger, new EventHandler() {
      public void execute(Object params) {
        write(1, "lea dx, [bp" + offset((int)params) + "]");
        write(1, "push dx");
        write(1, "call lirent");
      }
    }, "ASM");

    manager.register(YVM.Event.WriteBoolean, new EventHandler() {
      public void execute(Object params) {
        write(1, "call ecrbool");
      }
    }, "ASM");

    manager.register(YVM.Event.WriteInteger, new EventHandler() {
      public void execute(Object params) {
        write(1, "call ecrent");
      }
    }, "ASM");

    manager.register(YVM.Event.WriteString, new EventHandler() {
      public void execute(Object params) {
        String str = (String)params;
        str = str.substring(0, str.length() - 1) + "$\"";
        String id = "mess" + nextStr();

        write(0, ".DATA");
        write(1, "" + id + " DB " + str);
        write(0 , ".CODE");
        write(1, "lea dx, " + id + "");
        write(1, "push dx");
        write(1, "call ecrch");
      }
    }, "ASM");

    manager.register(YVM.Event.NewLine, new EventHandler() {
      public void execute(Object params) {
        write(1, "call ligsuiv");
      }
    }, "ASM");

    manager.register(YVM.Event.Label, new EventHandler() {
      public void execute(Object params) {
        write(0, params + ":");
      }
    }, "ASM");

    manager.register(YVM.Event.Jump, new EventHandler() {
      public void execute(Object params) {
        write(1, "jmp " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.JumpFalse, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop ax");
        write(1, "cmp ax, 0");
        write(1, "je " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.FunctionDeclaration, new EventHandler() {
      public void execute(Object params) {
        if ("main".equals(params)) {
          write("debut:");
          write(1, "STARTUPCODE");
        }

        write(0, params + ":");
      }
    }, "ASM");

    manager.register(YVM.Event.Return, new EventHandler() {
      public void execute(Object params) {
        write(1, "pop ax");
        write(1, "mov word ptr [bp" + offset((int)params) + "], ax");
      }
    }, "ASM");

    manager.register(YVM.Event.FunctionEnd, new EventHandler() {
      public void execute(Object params) {
        write(1, "leave");
        write(1, "ret " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.ReserveReturn, new EventHandler() {
      public void execute(Object params) {
        write(1, "sub sp, 2");
      }
    }, "ASM");

    manager.register(YVM.Event.FunctionCall, new EventHandler() {
      public void execute(Object params) {
        write(1, "call " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.Footer, new EventHandler() {
      public void execute(Object params) {
        write(1, "nop");
        write(1, "EXITCODE");
        write(1, "END debut");
      }
    }, "ASM");
  }

  protected int nextStr()
  {
    return m_strIndex++;
  }

  protected String offset(int offset)
  {
    return (offset < 0 ? "" : "+") + offset;
  }

  protected void write(int indent, String str)
  {
    if (indent > 0) ++indent;
    super.write(indent, str);
  }
}
