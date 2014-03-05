package Yaka;

public class IdConst extends Ident
{
  public IdConst(Type type, int value)
  {
    super(type, value);
  }

  public String toString()
  {
    return "[Constant] " + super.toString() + ": " + value();
  }
}
