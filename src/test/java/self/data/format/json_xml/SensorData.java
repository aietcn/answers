package self.data.format.json_xml;

import java.util.List;

/**
 * @author aiet
 */
public class SensorData {
  public List<Double> x;
  public List<Double> y;
  public List<Double> z;
  public List<Long> timestamp;
  public Sensor sensor;

  public SensorData() {
  }

  public SensorData(List<Double> x, List<Double> y, List<Double> z, List<Long> t) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.timestamp = t;
  }
}
