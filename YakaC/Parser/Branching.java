package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.ConditionException;
import java.util.ArrayDeque;

/**
 * Handles branchings, ie if(...) { } else { }
 */
public class Branching
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    BeginIf,
    Condition,
    BeginElse,
    EndIf;
  }

  /**
   * Represents a branch, stores its index and wether or not it has an else statement
   */
  protected static class Branch
  {
    public int index; /**< Branch index */
    public boolean hasElse = false; /**< Branch has an else statement */

    /**
     * @param i Branch index
     */
    public Branch(int i) { index = i; }
  }

  protected Context m_context; /**< Yaka context */
  protected int m_index; /**< Next branch index */
  protected ArrayDeque<Branch> m_branches; /**< Branches stack */

  /**
   * Constructor
   * @param context Yaka context
   */
  public Branching(Context context)
  {
    m_context = context;
    m_index = -1;
    m_branches = new ArrayDeque<Branch>();
  }

  /**
   * If statement
   */
  public void beginIf()
  {
    m_branches.push(new Branch(++m_index));
    m_context.eventManager().emit(Event.BeginIf, m_index);
  }

  /**
   * Parse the If condition
   * @throws ConditionException if it doesnt not evaluate to a boolean
   */
  public void condition() throws ConditionException
  {
    Ident.Type type = m_context.typeChecker().popType();

    if (Ident.Type.Boolean != type && Ident.Type.Error != type) {
      m_context.errorBag().add(new ConditionException(type));
    }

    m_context.eventManager().emit(Event.Condition, m_branches.peek().index);
  }

  /**
   * Else statement
   */
  public void beginElse()
  {
    if (0 == m_branches.size()) {
      throw new RuntimeException("No branchings left on the stack");
    }

    m_branches.peek().hasElse = true;
    m_context.eventManager().emit(Event.BeginElse, m_branches.peek().index);
  }

  /**
   * EndIf statement, generate an empty else if it was not added
   */
  public void endIf()
  {
    if (0 == m_branches.size()) {
      throw new RuntimeException("No branchings left on the stack");
    }

    if (!m_branches.peek().hasElse) {
      m_context.eventManager().emit(Event.BeginElse, m_branches.peek().index);
    }

    m_context.eventManager().emit(Event.EndIf, m_branches.pop().index);
  }
}
