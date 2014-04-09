package YakaC.Target;

import YakaC.javacc.Yaka;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Generic code generator
 */
public abstract class Writer
{
  protected PrintWriter m_writer; /**< Writer for outputting the code */

  /**
   * Constructor
   * @param os Output stream
   */
  public Writer(OutputStream os)
  {
    m_writer = new java.io.PrintWriter(os, true);
  }

  /**
   * Output a string
   * @param str String
   */
  protected void write(String str)
  {
    m_writer.println(str);
  }

  /**
   * Output a string with an indentation level
   * @param indent Indentation level
   * @param str String
   */
  protected void write(int indent, String str)
  {
    String tabs = new String(new char[indent]).replace("\0", "  ");
    write(tabs + str);
  }
}
