package self.data.format.json_xml;

/**
 * @author aiet
 */
public enum Sensor {
  ACCELERATION(1), MAGNETISM(2);
  private int i;
  @Override
  public String toString(){
    return i+"";
  }

  private Sensor(int i) {
    this.i = i;
  }
}
