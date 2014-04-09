package YakaC.Exception;

/**
 * Trying to use return in the main function
 */
public class MainReturnException extends YakaException
{
  /**
   * Constructor
   */
  public MainReturnException()
  {
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Return statement illegal in main function";
  }
}
