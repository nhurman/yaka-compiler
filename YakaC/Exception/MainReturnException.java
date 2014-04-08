package YakaC.Exception;

public class MainReturnException extends YakaException
{
  public MainReturnException()
  {

  }

  public String toString()
  {
    return "Return statement illegal in main function";
  }
}
