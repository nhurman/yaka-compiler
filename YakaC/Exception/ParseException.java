package YakaC.Exception;

public class ParseException extends YakaException
{
  protected String m_message;

  public ParseException(String message)
  {
    m_message = message;
  }

  public String toString()
  {
    return "Parse error: " + m_message;
  }
}
