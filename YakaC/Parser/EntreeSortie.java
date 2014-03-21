package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Parser.Ident;
import YakaC.Exception.IOReadException;
import YakaC.Exception.TypeMismatchException;
import YakaC.Exception.UndefinedIdentException;
import YakaC.Target.YVM;

public class EntreeSortie
{
  public static enum Event implements YakaC.Event.Event
  {
    Read,
    WriteBoolean,
    WriteInteger,
    WriteString,
    NewLine;
  }

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TabIdent m_tabIdent;
  protected TypeChecker m_typeChecker;

  public EntreeSortie(ErrorBag errors, EventManager eventManager, TabIdent tabIdent, TypeChecker typeChecker)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_tabIdent = tabIdent;
    m_typeChecker = typeChecker;
  }

  public void read(String name) throws UndefinedIdentException, IOReadException
  {
    if (!m_tabIdent.exists(name)) {
      m_errors.add(new UndefinedIdentException(name));
      return;
    }

    Ident ident = m_tabIdent.find(name);
    if (Ident.Kind.Variable != ident.kind() ||
        Ident.Type.Integer != ident.type()) {
      m_errors.add(new IOReadException(name, ident));
      return;
    }

    m_eventManager.emit(Event.Read, new Integer(ident.value()));
  }

  public void write()
  {
    Ident.Type type = m_typeChecker.lastType();
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
      m_eventManager.emit(event);
    }
  }

  public void write(String str)
  {
    m_eventManager.emit(Event.WriteString, str);
  }

  public void newLine()
  {
    m_eventManager.emit(Event.NewLine);
  }
}
