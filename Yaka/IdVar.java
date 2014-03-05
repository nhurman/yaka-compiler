package Yaka;

public class IdVar extends Ident
{
  public IdVar(Type type, int value)
  {
    super(type, value);
  }

  public String toString()
  {
    return "[Variable] " + super.toString() + " offset: " + value();
  }
}
