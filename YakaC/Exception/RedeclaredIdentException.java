package YakaC.Exception;

public class RedeclaredIdentException extends YakaException
{
  protected String m_name;

  public RedeclaredIdentException(String name)
  {
    m_name = name;
  }

  public String toString()
  {
    return "Redeclared identifier: " + m_name;
  }
}
