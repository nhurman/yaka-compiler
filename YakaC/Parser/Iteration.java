package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.IterationException;
import java.util.ArrayDeque;

public class Iteration
{
  public static enum Event implements YakaC.Event.Event
  {
    BeginFor,
    Condition,
    EndFor;
  }

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TypeChecker m_typeChecker;
  protected int m_index;
  protected ArrayDeque<Integer> m_loops;

  public Iteration(ErrorBag errors, EventManager eventManager, TypeChecker typeChecker)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_typeChecker = typeChecker;
    m_index = -1;
    m_loops = new ArrayDeque<Integer>();
  }

  public void beginFor()
  {
    m_loops.push(++m_index);
    m_eventManager.emit(Event.BeginFor, m_index);
  }

  public void condition() throws IterationException
  {
    Ident.Type type = m_typeChecker.lastType();

    if (Ident.Type.Boolean != type && Ident.Type.Error != type) {
      m_errors.add(new IterationException(type));
    }

    m_eventManager.emit(Event.Condition, m_loops.peek());
  }

  public void endFor()
  {
    if (0 == m_loops.size()) {
      throw new RuntimeException("No labels left on the stack");
    }

    m_eventManager.emit(Event.EndFor, m_loops.pop());
  }
}
