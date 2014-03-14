package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.ConditionException;
import java.util.ArrayDeque;

public class Branching
{
  public static enum Event implements YakaC.Event.Event
  {
    BeginIf,
    Condition,
    BeginElse,
    EndIf;
  }

  protected static class Branch
  {
    public int index;
    public boolean hasElse = false;

    public Branch(int i) { index = i; }
  }

  protected ErrorBag m_errors;
  protected EventManager m_eventManager;
  protected TypeChecker m_typeChecker;
  protected int m_index;
  protected ArrayDeque<Branch> m_branches;

  public Branching(ErrorBag errors, EventManager eventManager, TypeChecker typeChecker)
  {
    m_errors = errors;
    m_eventManager = eventManager;
    m_typeChecker = typeChecker;
    m_index = -1;
    m_branches = new ArrayDeque<Branch>();
  }

  public void beginIf()
  {
    m_branches.push(new Branch(++m_index));
    m_eventManager.emit(Event.BeginIf, m_index);
  }

  public void condition() throws ConditionException
  {
    Ident.Type type = m_typeChecker.lastType();

    if (Ident.Type.Boolean != type && Ident.Type.Error != type) {
      m_errors.add(new ConditionException(type));
    }

    m_eventManager.emit(Event.Condition, m_branches.peek().index);
  }

  public void beginElse()
  {
    m_branches.peek().hasElse = true;
    m_eventManager.emit(Event.BeginElse, m_branches.peek().index);
  }

  public void endIf()
  {
    if (0 == m_branches.size()) {
      throw new RuntimeException("No labels left on the stack");
    }

    if (!m_branches.peek().hasElse) {
      m_eventManager.emit(Event.BeginElse, m_branches.peek().index);
    }

    m_eventManager.emit(Event.EndIf, m_branches.pop().index);
  }
}
