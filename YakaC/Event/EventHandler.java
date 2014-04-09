package YakaC.Event;

/**
 * Implemented by event callbacks
 */
public abstract class EventHandler
{
  /**
   * Several events can share the same tag to group them
   */
  public String tag;

  /**
   * Event handler code
   * @param params Event parameters
   */
  public abstract void execute(Object params);
}
