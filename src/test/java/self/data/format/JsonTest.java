package self.data.format;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import self.data.format.json_xml.JsonData;
import self.data.format.json_xml.SensorData;
import self.data.format.json_xml.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;
import static self.data.format.json_xml.Sensor.ACCELERATION;
import static self.data.format.json_xml.Sensor.MAGNETISM;

/**
 * @author aiet
 */
public class JsonTest {

  @Test
  public void testSerializeSize() throws Exception {
    JsonData data = new JsonData();
    data.timestamp = System.currentTimeMillis();
    data.type = Type.UPLOAD;
    data.uid = "guava";
    SensorData sensorData = new SensorData(
        asList(0.0, 3.3), asList(1.1, 4.4), asList(2.2, 5.5), asList(System.currentTimeMillis(), System.currentTimeMillis()));
    sensorData.sensor = ACCELERATION;
    data.list = singletonList(sensorData);
    byte[] bytes = JSON.toJSONBytes(data, SerializerFeature.WriteEnumUsingToString);
    System.out.println(JSON.toJSONString(data));
    assertTrue(bytes.length==160);
  }

  @Test
  public void testSerializeTime() throws Exception {
    JsonData data = new JsonData();
    data.timestamp = System.currentTimeMillis();
    data.type = Type.UPLOAD;
    data.uid = "guava";
    SensorData sensorData = new SensorData(
        asList(0.0, 3.3), asList(1.1, 4.4), asList(2.2, 5.5), asList(System.currentTimeMillis(), System.currentTimeMillis()));
    sensorData.sensor = ACCELERATION;
    data.list = singletonList(sensorData);
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);

      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        byte[] bytes = JSON.toJSONBytes(data, SerializerFeature.WriteEnumUsingToString);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>1100&&avgtime<1300);
  }

  @Test
  public void testDeserializationGetType() throws Exception {
    JsonData data = new JsonData();
    data.timestamp = System.currentTimeMillis();
    data.type = Type.UPLOAD;
    data.uid = "guava";
    SensorData sensorData = new SensorData(
        asList(0.0, 3.3), asList(1.1, 4.4), asList(2.2, 5.5), asList(System.currentTimeMillis(), System.currentTimeMillis()));
    sensorData.sensor = ACCELERATION;
    data.list = singletonList(sensorData);
    byte[] bytes = JSON.toJSONBytes(data);

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {

      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        JsonData jsonData = JSON.parseObject(bytes, JsonData.class);
        Type type = jsonData.type;
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>1450&&avgtime<1520);
  }

  @Test
  public void testDeserializationGetListData() throws Exception {

    JsonData data = new JsonData();
    data.timestamp = System.currentTimeMillis();
    data.type = Type.UPLOAD;
    data.uid = "guava";
    SensorData sensorData = new SensorData(
        asList(0.0, 3.3), asList(1.1, 4.4), asList(2.2, 5.5), asList(System.currentTimeMillis(), System.currentTimeMillis()));
    sensorData.sensor = ACCELERATION;
    data.list = singletonList(sensorData);
    byte[] bytes = JSON.toJSONBytes(data);

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {

      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        JsonData jsonData = JSON.parseObject(bytes, JsonData.class);
        double x = jsonData.list.get(0).x.get(0);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>1450&&avgtime<1520);

  }

  private JsonData largeData(){
    JsonData data = new JsonData();
    data.timestamp = System.currentTimeMillis();
    data.type = Type.UPLOAD;
    data.uid = "guava";
    SensorData accSensorData = new SensorData(
        asList(0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30),
        asList(1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31),
        asList(2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32),
        asList(
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()
        )
    );
    accSensorData.sensor = ACCELERATION;
    SensorData magSensorData = new SensorData(
        asList(0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30),
        asList(1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31),
        asList(2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32),
        asList(
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()
        )
    );
    magSensorData.sensor = MAGNETISM;
    data.list = Arrays.asList(accSensorData, magSensorData);
    return data;
  }

  @Test
  public void testSerializeSizeLargerData() throws Exception {
    byte[] bytes = JSON.toJSONBytes(largeData(), SerializerFeature.WriteEnumUsingToString);
    assertTrue(bytes.length==813);
  }

  @Test
  public void testSerializeTimeLargerData() throws Exception {
    JsonData data = largeData();
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);

      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        byte[] bytes = JSON.toJSONBytes(data, SerializerFeature.WriteEnumUsingToString);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>9000&&avgtime<9300); //9150
  }

  @Test
  public void testDeserializationGetTypeLargerData() throws Exception {
    JsonData data = largeData();
    byte[] bytes = JSON.toJSONBytes(data);
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        JsonData jsonData = JSON.parseObject(bytes, JsonData.class);
        Type type = jsonData.type;
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>9400&&avgtime<9650); //9580
  }

  @Test
  public void testDeserializationGetListDataLargerData() throws Exception {
    JsonData data = largeData();
    byte[] bytes = JSON.toJSONBytes(data);
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        JsonData jsonData = JSON.parseObject(bytes, JsonData.class);
        double x = jsonData.list.get(0).x.get(0);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>9400&&avgtime<9650); //9500

  }

}
