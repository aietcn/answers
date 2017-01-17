package self.data.format;

import org.junit.Test;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Builder;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.SensorData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Sensor.ACCELERATION;
import static self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Type.UPLOAD;
import static self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.newBuilder;

/**
 * @author aiet
 *         comparison between different data formats serialization and deserialization
 */
public class ProtobufTest {

  @Test
  public void testSerializeSize() throws Exception {
    Builder builder = newBuilder();
    builder.setType(UPLOAD);
    builder.setUid("guava");
    builder.setTimestamp(System.currentTimeMillis());
    SensorData.Builder dataBuilder = SensorData.newBuilder();
    dataBuilder.setSensor(ACCELERATION);
    dataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
    dataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
    builder.addList(dataBuilder.build());
    ProtobufData data = builder.build();
    byte[] bytes = data.toByteArray();
    System.out.println(bytes.length);
    assertTrue(bytes.length == 88);
  }

  @Test
  public void testSerializeTime() throws Exception {
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        Builder builder = newBuilder();
        long before = System.nanoTime();
        builder.setType(UPLOAD);
        builder.setUid("guava");
        builder.setTimestamp(System.currentTimeMillis());
        SensorData.Builder dataBuilder = SensorData.newBuilder();
        dataBuilder.setSensor(ACCELERATION);
        dataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
        dataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
        builder.addList(dataBuilder.build());
        ProtobufData data = builder.build();
        byte[] bytes = data.toByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 470 && avgtime < 480);
  }

  @Test
  public void testSerializeTimePrebuild() throws Exception {
    /* CAUTION! prebuilt data builder does not make things better */
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      Builder builder = newBuilder();
      SensorData.Builder dataBuilder = SensorData.newBuilder();
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        builder.setType(UPLOAD);
        builder.setUid("guava");
        builder.setTimestamp(System.currentTimeMillis());
        dataBuilder.setSensor(ACCELERATION);
        dataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
        dataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
        builder.addList(dataBuilder.build());
        ProtobufData data = builder.build();
        byte[] bytes = data.toByteArray();
        times.add(System.nanoTime() - before);
        builder.clear();
        dataBuilder.clear();
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    assertTrue(avgtime > 560 && avgtime < 570);
  }

  @Test
  public void testDeserializationGetType() throws Exception {
    Builder builder = newBuilder();
    builder.setType(UPLOAD);
    builder.setUid("guava");
    builder.setTimestamp(System.currentTimeMillis());
    SensorData.Builder dataBuilder = SensorData.newBuilder();
    dataBuilder.setSensor(ACCELERATION);
    dataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
    dataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
    builder.addList(dataBuilder.build());
    ProtobufData data = builder.build();
    byte[] bytes = data.toByteArray();

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        ProtobufData protobufData = ProtobufData.parseFrom(bytes);
        protobufData.getType();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>380&&avgtime<390);
  }

  @Test
  public void testDeserializationGetListData() throws Exception {
    Builder builder = newBuilder();
    builder.setType(UPLOAD);
    builder.setUid("guava");
    builder.setTimestamp(System.currentTimeMillis());
    SensorData.Builder dataBuilder = SensorData.newBuilder();
    dataBuilder.setSensor(ACCELERATION);
    dataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
    dataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
    builder.addList(dataBuilder.build());
    ProtobufData data = builder.build();
    byte[] bytes = data.toByteArray();

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        ProtobufData protobufData = ProtobufData.parseFrom(bytes);
        protobufData.getList(0).getX(0);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>390&&avgtime<400);

  }
}
