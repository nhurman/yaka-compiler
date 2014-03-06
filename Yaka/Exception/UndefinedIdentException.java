package Yaka.Exception;

public class UndefinedIdentException extends YakaException
{
  protected String m_name;

  public UndefinedIdentException(String name)
  {
    m_name = name;
  }

  public String toString()
  {
    return "Undefined identifier: " + m_name;
  }
}
