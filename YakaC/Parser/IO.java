package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Parser.Ident;
import YakaC.Exception.IOReadException;
import YakaC.Exception.TypeMismatchException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

/**
 * Input/Output operations
 */
public class IO
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    Read,
    WriteBoolean,
    WriteInteger,
    WriteString,
    NewLine;
  }

  protected Context m_context; /**< Yaka context */

  /**
   * Constructor
   * @param context Yaka context
   */
  public IO(Context context)
  {
    m_context = context;
  }

  /**
   * Read a value from user input
   * @param name The identifier that will store the value
   */
  public void read(String name) throws UndefinedIdentException, IOReadException
  {
    if (!m_context.locals().exists(name)) {
      m_context.errorBag().add(new UndefinedIdentException(name));
      return;
    }

    Ident ident = m_context.locals().find(name);
    if (Ident.Kind.Variable != ident.kind() ||
        Ident.Type.Integer != ident.type()) {
      m_context.errorBag().add(new IOReadException(name, ident));
      return;
    }

    m_context.eventManager().emit(Event.Read, new Integer(ident.value()));
  }

  /**
   * Output a value
   */
  public void write()
  {
    Ident.Type type = m_context.typeChecker().popType();
    Event event = null;

    if (Ident.Type.Boolean == type) {
      event = Event.WriteBoolean;
    }
    else if (Ident.Type.Integer == type) {
      event = Event.WriteInteger;
    }
    else if (Ident.Type.Error != type) {
      throw new RuntimeException("Unhandled type " + type);
    }

    if (null != event) {
      m_context.eventManager().emit(event);
    }
  }

  /**
   * Output a string
   * @param str String
   */
  public void write(String str)
  {
    m_context.eventManager().emit(Event.WriteString, str);
  }

  /**
   * Start a new line
   */
  public void newLine()
  {
    m_context.eventManager().emit(Event.NewLine);
  }
}
