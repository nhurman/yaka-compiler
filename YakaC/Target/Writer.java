package YakaC.Target;

import YakaC.javacc.Yaka;
import java.io.OutputStream;
import java.io.PrintWriter;

public abstract class Writer
{
  protected PrintWriter m_writer;

  public Writer(final Yaka yaka, OutputStream os)
  {
    m_writer = new java.io.PrintWriter(os, true);
  }

  protected void write(String str)
  {
    m_writer.println(str);
  }

  protected void write(int indent, String str)
  {
    String tabs = new String(new char[indent]).replace("\0", "  ");
    write(tabs + str);
  }
}
