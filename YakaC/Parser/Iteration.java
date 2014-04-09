package YakaC.Parser;

import YakaC.Event.EventManager;
import YakaC.Exception.ConditionException;
import java.util.ArrayDeque;

/**
 * Handles iterations, ie while(...) { }
 */
public class Iteration
{
  /** Events */
  public static enum Event implements YakaC.Event.Event
  {
    BeginFor,
    Condition,
    EndFor;
  }

  protected Context m_context; /**< Yaka context */
  protected int m_index; /**< Next iteration index */
  protected ArrayDeque<Integer> m_loops; /**< Iterations stack */

  /**
   * Constructor
   * @param context Yaka context
   */
  public Iteration(Context context)
  {
    m_context = context;
    m_index = -1;
    m_loops = new ArrayDeque<Integer>();
  }

  /**
   * Start a for loop
   */
  public void beginFor()
  {
    m_loops.push(++m_index);
    m_context.eventManager().emit(Event.BeginFor, m_index);
  }

  /**
   * Set the loop's condition
   */
  public void condition() throws ConditionException
  {
    Ident.Type type = m_context.typeChecker().peekType();

    if (Ident.Type.Boolean != type && Ident.Type.Error != type) {
      m_context.errorBag().add(new ConditionException(type));
    }

    m_context.eventManager().emit(Event.Condition, m_loops.peek());
  }

  /**
   * End of for loop
   */
  public void endFor()
  {
    if (0 == m_loops.size()) {
      throw new RuntimeException("No labels left on the stack");
    }

    m_context.eventManager().emit(Event.EndFor, m_loops.pop());
  }
}
