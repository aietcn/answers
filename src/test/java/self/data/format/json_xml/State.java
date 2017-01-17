package self.data.format.json_xml;

/**
 * @author aiet
 */
public enum State {
  ERROR(-1), SUCCESS(1);
  private int i;
  @Override
  public String toString(){
    return i+"";
  }
  private State(int i){
    this.i = i;
  }
}
