package YakaC.Exception;

import YakaC.Parser.IdFunct;

public class SignatureException extends YakaException
{
  protected IdFunct m_f1;
  protected IdFunct m_f2;

  public SignatureException(IdFunct f1, IdFunct f2)
  {
    m_f1 = f1;
    m_f2 = f2;
  }

  public String toString()
  {
    return "Function signature is " + m_f1 + ", got " + m_f2;
  }
}
