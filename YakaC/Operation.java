package YakaC;

public class Operation
{
  public Ident.Type t1;
  public Ident.Type t2;
  public Expression.Operator op;

  public Operation(Ident.Type t1, Expression.Operator op, Ident.Type t2)
  {
    this.t1 = t1;
    this.t2 = t2;
    this.op = op;
  }
}
