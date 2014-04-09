package YakaC.Exception;

import YakaC.Parser.IdFunct;

/**
 * Function name matches but the arguments don't
 */
public class SignatureException extends YakaException
{
  protected IdFunct m_f1; /**< Expected function */
  protected IdFunct m_f2; /**< Called function */

  /**
   * @param f1 Expected function
   * @param f2 Called function
   */
  public SignatureException(IdFunct f1, IdFunct f2)
  {
    m_f1 = f1;
    m_f2 = f2;
  }

  /**
   * Get a string representation of the exception
   * @return String
   */
  public String toString()
  {
    return "Function signature is " + m_f1 + ", got " + m_f2;
  }
}
