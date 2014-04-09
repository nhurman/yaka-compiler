package YakaC.Parser;

import YakaC.Exception.YakaException;
import YakaC.Event.EventManager;
import YakaC.javacc.Yaka;
import java.util.ArrayDeque;

/**
 * Stores an error list
 */
public class ErrorBag
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    Error;
  }

  /**
   * Internal error representation, adds some metadata to the error message
   */
  protected static class Error
  {
    public YakaException exception; /**< Exception */
    public String message; /**< Exception message */
    public int line; /**< Error line in yaka file */
    public int column; /**< Error column in yaka file */

    /**
     * Constructor
     * @param e Exception
     * @param m Message
     * @param l Line
     * @param c Column
     */
    public Error(YakaException e, String m, int l, int c)
    {
      this.exception = e;
      this.message = m;
      this.line = l;
      this.column = c;
    }
  }

  protected Context m_context; /**< Yaka context */
  protected boolean m_throw; /**< Throw exceptions */
  protected ArrayDeque<Error> m_errors; /**< Errors list */

  /**
   * Constructor
   * @param context Yaka context
   * @param throwExceptions Throw exceptions when errors are added?
   */
  public ErrorBag(Context context, boolean throwExceptions)
  {
    m_context = context;
    m_throw = throwExceptions;
    m_errors = new ArrayDeque<Error>();
  }

  /**
   * Add an error
   * @param exception Error
   * @param message Exception additional details
   */
  public void add(YakaException exception, String message)
  {
    Error e = new Error(exception, message,
      m_context.yaka().token.endLine,
      m_context.yaka().token.endColumn);
    m_errors.add(e);
    m_context.eventManager().emit(Event.Error, e);

    /*if (m_throw) {
      throw exception;
    }*/
  }

  /**
   * Add an error
   * @param exception Error
   */
  public void add(YakaException exception)
  {
    add(exception, exception.toString());
  }

  /**
   * Format the error list into a string
   * @return String
   */
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
