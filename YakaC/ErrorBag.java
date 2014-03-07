package YakaC;

import YakaC.Exception.YakaException;
import YakaC.Event.Event;
import YakaC.Event.EventManager;
import YakaC.javacc.Yaka;
import java.util.ArrayDeque;

public class ErrorBag
{
  protected static class Error
  {
    public YakaException exception;
    public String message;
    public int line;
    public int column;

    public Error(YakaException e, String m, int l, int c)
    {
      this.exception = e;
      this.message = m;
      this.line = l;
      this.column = c;
    }
  }

  protected EventManager m_eventManager;
  protected boolean m_throw;
  protected ArrayDeque<Error> m_errors;

  public ErrorBag(EventManager eventManager, boolean throwExceptions)
  {
    m_eventManager = eventManager;
    m_throw = throwExceptions;
    m_errors = new ArrayDeque<Error>();
  }

  public void add(YakaException exception, String message)
  {
    Error e = new Error(exception, message,
        Yaka.token.endLine,
        Yaka.token.endColumn);
    m_errors.add(e);
    m_eventManager.emit(Event.Error, e);

    /*if (m_throw) {
      throw exception;
    }*/
  }

  public void add(YakaException exception)
  {
    add(exception, exception.toString());
  }

  public String toString()
  {
    if (0 == m_errors.size())
      return "";

    String out = "" + m_errors.size() + " error" + (m_errors.size() > 1 ? "s" : "") + ":\n";
    for (Error e: m_errors) {
      out += " - [Line " + e.line + ", Column " + e.column + "] " + e.message + "\n";
    }

    return out;
  }
}