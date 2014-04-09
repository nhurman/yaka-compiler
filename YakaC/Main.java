package YakaC;

import YakaC.javacc.Yaka;
import YakaC.Target.YVM;
import YakaC.Target.Tasm;

/**
 * Main class for invoking the compiler
 */
public class Main
{
  /**
   * Main function
   * @param args Console arguments
   */
  public static void main(String[] args)
  {
    java.io.InputStream in;
    java.io.OutputStream out;

    if (args.length < 0 || args.length > 2) {
      System.err.println("Usage:");
      System.err.println("- java " + Main.class.getName() + " < yaka > yvm");
      System.err.println("- java " + Main.class.getName() + " yaka > yvm");
      System.err.println("- java " + Main.class.getName() + " yaka yvm");

      return;
    }

    // Read Yaka from stdin and write YVM to stdout
    in = System.in;
    out = System.out;

    try {
      if (args.length >= 1) {
        // Read Yaka from file and write YVM to stdout
        in = new java.io.FileInputStream(args[0]);
      }

      if (args.length == 2) {
        // Read Yaka from file and write YVM to file
        java.io.File fp = new java.io.File(args[1]);
        out = new java.io.FileOutputStream(fp);
      }
    } catch (java.io.FileNotFoundException e) {
      System.err.println(e.getMessage());
      return;
    }

    Yaka yaka = new Yaka(in);
    yaka.init();

    YVM yvm = new YVM(yaka.context(), out);
    Tasm asm = new Tasm(yaka.context(), out);

    try {
      yaka.analyse();
    } catch (YakaC.Exception.YakaException e) {
      System.out.println("Parse error: " + e);
    } catch (YakaC.javacc.ParseException e) {
      yaka.errors().add(
        new YakaC.Exception.ParseException(e.getMessage()));
    } finally {
      System.err.print(yaka.errors());
    }
  }
}
