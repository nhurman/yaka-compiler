package YakaC.Event;

public abstract class EventHandler
{
  public String tag;

  public abstract void execute(Object params);
}
