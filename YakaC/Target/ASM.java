package YakaC.Target;

import YakaC.javacc.Yaka;
import YakaC.Event.*;
import YakaC.Event.Event;
import YakaC.Parser.Expression.Operator;
import YakaC.Parser.Ident;
import YakaC.Parser.TabIdent;
import YakaC.Target.YVM;
import YakaC.Exception.UndefinedIdentException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ASM
{
  public static final int StackValueSize = 2;

  protected PrintWriter m_writer;
  protected boolean m_error;

  public ASM(final Yaka yaka, OutputStream os)
  {
    m_writer = new java.io.PrintWriter(os, true);
    m_error = false;

    final EventManager manager = yaka.eventManager();

    manager.register(YakaC.Parser.ErrorBag.Event.Error, new EventHandler() {
      public void execute(Object params) {
        manager.unregister("ASM");
        m_writer.println("Stopping ASM code generation due to error");
      }
    }, "ASM");

    manager.register(YVM.Event.Header, new EventHandler() {
      public void execute(Object params) {
        m_writer.println(".model SMALL\n.586\n.CODE\ndebut:\nSTARTUPCODE");
      }
    }, "ASM");

    manager.register(YVM.Event.StackDefinition, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("mov bp, sp\nsub sp, " + (Integer)params);
      }
    }, "ASM");

    manager.register(YVM.Event.IConst, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("push " + params);
      }
    }, "ASM");

    manager.register(YVM.Event.ILoad, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop ax\nmov word ptr[bp" + params + "], ax");
      }
    }, "ASM");


    manager.register(YVM.Event.IAdd, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\nadd ax, bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.ISub, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\nsub ax, bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IMul, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\nimul bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IDiv, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncwd\nidiv bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IInf, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\njge $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IInfEgal, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\njg $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.ISup, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\njle $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.ISupEgal, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\njl $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IEgal, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\njne $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IDiff, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\ncmp ax, bx\nje $+6\npush -1\njmp $+4\npush 0");
      }
    }, "ASM");

    manager.register(YVM.Event.IAnd, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\nand ax, bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.IOr, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop bx\npop ax\nor ax, bx\npush ax");
      }
    }, "ASM");

    manager.register(YVM.Event.INeg, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("pop ax\nnot ax");
      }
    }, "ASM");

    manager.register(YVM.Event.Footer, new EventHandler() {
      public void execute(Object params) {
        m_writer.println("nop\nEXITCODE\nEND debut");
      }
    }, "ASM");
  }
}
