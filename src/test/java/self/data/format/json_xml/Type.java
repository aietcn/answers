package self.data.format.json_xml;

/**
 * @author aiet
 */
public enum Type {
  PING(1), PONG(2), UPLOAD(3), RSP_UPLOAD(4), SIGNUP(5), RSP_SIGNUP(6),
  SIGNIN(7), RSP_SIGNIN(8);
  private int i;
  @Override
  public String toString(){
    return i+"";
  }
  private Type(int i){
    this.i = i;
  }
}
