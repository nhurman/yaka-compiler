package YakaC.Event;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayDeque;

public class EventManager
{
  protected HashMap<Event, ArrayDeque<EventHandler>> m_handlers;

  public EventManager()
  {
    m_handlers = new HashMap<Event, ArrayDeque<EventHandler>>();
  }

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

  public boolean register(Event event, EventHandler handler)
  {
    return register(event, handler, "");
  }

  public boolean unregister(Event event, EventHandler handler)
  {
    if (!m_handlers.containsKey(event)) {
      return false;
    }

    return m_handlers.get(event).remove(handler);
  }

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

  public boolean unregister(String tag)
  {
    boolean status = false;

    for (Event event: m_handlers.keySet()) {
      status = unregister(event, tag) || status;
    }

    return status;
  }

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

  public boolean emit(Event event)
  {
    return emit(event, null);
  }
}
