package YakaC.Event;

import java.util.HashMap;
import java.util.ArrayDeque;

/**
 * Implements an event mecanism
 */
public class EventManager
{
  /** List of event handlers per event */
  protected HashMap<Event, ArrayDeque<EventHandler>> m_handlers;

  /**
   * Constructor
   */
  public EventManager()
  {
    m_handlers = new HashMap<Event, ArrayDeque<EventHandler>>();
  }

  /**
   * Register an event handler
   * @param event Event
   * @param handler Handler
   * @param tag Tag
   * @return True if the handler was registered
   */
  public boolean register(Event event, EventHandler handler, String tag)
  {
    if (!m_handlers.containsKey(event)) {
      m_handlers.put(event, new ArrayDeque<EventHandler>());
    }

    if (m_handlers.get(event).contains(handler)) {
      return false;
    }

    handler.tag = tag;
    m_handlers.get(event).add(handler);
    return true;
  }

  /**
   * Register an event handler
   * @param event Event
   * @param handler Handler
   * @return True if the handler was registered
   */
  public boolean register(Event event, EventHandler handler)
  {
    return register(event, handler, "");
  }

  /**
   * Unregister an event handler
   * @param event Event
   * @param handler Handler
   * @return True if the handler was unregistered
   */
  public boolean unregister(Event event, EventHandler handler)
  {
    if (!m_handlers.containsKey(event)) {
      return false;
    }

    return m_handlers.get(event).remove(handler);
  }

  /**
   * Unregister a group of event handlers using their tag
   * @param event Event
   * @param tag Tag
   * @return True if the handlers were unregistered
   */
  public boolean unregister(Event event, String tag)
  {
    boolean status = false;

    if (!m_handlers.containsKey(event)) {
      return false;
    }

    for (EventHandler handler: m_handlers.get(event)) {
      if (tag.equals(handler.tag)) {
        status = unregister(event, handler) || status;
      }
    }

    return status;
  }

  /**
   * Unregister a group of event handlers using their tag
   * @param tag Tag
   * @return True if the handlers were unregistered
   */
  public boolean unregister(String tag)
  {
    boolean status = false;

    for (Event event: m_handlers.keySet()) {
      status = unregister(event, tag) || status;
    }

    return status;
  }

  /**
   * Emit an event, calling all its handlers
   * @param event Event
   * @param params Event parameters
   * @return True if handlers were executed
   */
  public boolean emit(Event event, Object params)
  {
    if (!m_handlers.containsKey(event)) {
      return false;
    }

    for (EventHandler handler: m_handlers.get(event)) {
      handler.execute(params);
    }

    return m_handlers.get(event).size() > 0;
  }

  /**
   * Emit an event, calling all its handlers
   * @param event Event
   * @return True if handlers were executed
   */
  public boolean emit(Event event)
  {
    return emit(event, null);
  }
}
