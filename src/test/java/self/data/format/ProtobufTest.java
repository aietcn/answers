package self.data.format;

import org.junit.Test;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Builder;
import self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.SensorData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Sensor.ACCELERATION;
import static self.data.format.protobuf.ProtobufDataOuterClass.ProtobufData.Sensor.MAGNETISM;
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
    assertTrue(avgtime > 440 && avgtime < 490);
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
    assertTrue(avgtime > 560 && avgtime < 590);
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
    assertTrue(avgtime>370&&avgtime<400);
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
    assertTrue(avgtime>390&&avgtime<420);
  }

  private ProtobufData largeData(){
    Builder builder = newBuilder();
    builder.setType(UPLOAD);
    builder.setUid("guava");
    builder.setTimestamp(System.currentTimeMillis());
    SensorData.Builder accDataBuilder = SensorData.newBuilder();
    accDataBuilder.setSensor(ACCELERATION);
    accDataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(6.6).addY(7.7).addZ(8.8).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(9.9).addY(10.10).addZ(11.11).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(12.12).addY(13.13).addZ(14.14).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(15.15).addY(16.16).addZ(17.17).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(18.18).addY(19.19).addZ(20.20).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(21.21).addY(22.22).addZ(23.23).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(24.24).addY(25.25).addZ(26.26).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(27.27).addY(28.28).addZ(29.29).addTimestamp(System.currentTimeMillis());
    accDataBuilder.addX(30.30).addY(31.31).addZ(32.32).addTimestamp(System.currentTimeMillis());
    SensorData.Builder magDataBuilder = SensorData.newBuilder();
    magDataBuilder.setSensor(MAGNETISM);
    magDataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(6.6).addY(7.7).addZ(8.8).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(9.9).addY(10.10).addZ(11.11).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(12.12).addY(13.13).addZ(14.14).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(15.15).addY(16.16).addZ(17.17).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(18.18).addY(19.19).addZ(20.20).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(21.21).addY(22.22).addZ(23.23).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(24.24).addY(25.25).addZ(26.26).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(27.27).addY(28.28).addZ(29.29).addTimestamp(System.currentTimeMillis());
    magDataBuilder.addX(30.30).addY(31.31).addZ(32.32).addTimestamp(System.currentTimeMillis());
    builder.addList(magDataBuilder.build()).addList(accDataBuilder.build());
    return builder.build();
  }


  @Test
  public void testSerializeSizeLargeData() throws Exception {
    ProtobufData data = largeData();
    byte[] bytes = data.toByteArray();
    System.out.println(bytes.length);
    assertTrue(bytes.length == 774);
  }

  @Test
  public void testSerializeTimeLargeData() throws Exception {
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        Builder builder = newBuilder();
        builder.setType(UPLOAD);
        builder.setUid("guava");
        builder.setTimestamp(System.currentTimeMillis());
        SensorData.Builder accDataBuilder = SensorData.newBuilder();
        accDataBuilder.setSensor(ACCELERATION);
        accDataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(6.6).addY(7.7).addZ(8.8).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(9.9).addY(10.10).addZ(11.11).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(12.12).addY(13.13).addZ(14.14).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(15.15).addY(16.16).addZ(17.17).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(18.18).addY(19.19).addZ(20.20).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(21.21).addY(22.22).addZ(23.23).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(24.24).addY(25.25).addZ(26.26).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(27.27).addY(28.28).addZ(29.29).addTimestamp(System.currentTimeMillis());
        accDataBuilder.addX(30.30).addY(31.31).addZ(32.32).addTimestamp(System.currentTimeMillis());
        SensorData.Builder magDataBuilder = SensorData.newBuilder();
        magDataBuilder.setSensor(MAGNETISM);
        magDataBuilder.addX(0.0).addY(1.1).addZ(2.2).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(3.3).addY(4.4).addZ(5.5).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(6.6).addY(7.7).addZ(8.8).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(9.9).addY(10.10).addZ(11.11).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(12.12).addY(13.13).addZ(14.14).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(15.15).addY(16.16).addZ(17.17).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(18.18).addY(19.19).addZ(20.20).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(21.21).addY(22.22).addZ(23.23).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(24.24).addY(25.25).addZ(26.26).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(27.27).addY(28.28).addZ(29.29).addTimestamp(System.currentTimeMillis());
        magDataBuilder.addX(30.30).addY(31.31).addZ(32.32).addTimestamp(System.currentTimeMillis());
        builder.addList(magDataBuilder.build()).addList(accDataBuilder.build());
        ProtobufData data = builder.build();
        byte[] bytes = data.toByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 3600 && avgtime < 3800); //3750
  }

  @Test
  public void testDeserializationGetTypeLargeData() throws Exception {
    ProtobufData data = largeData();
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
    assertTrue(avgtime>2050&&avgtime<2200); //2100
  }

  @Test
  public void testDeserializationGetListDataLargeData() throws Exception {
    ProtobufData data = largeData();
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
    assertTrue(avgtime>2050&&avgtime<2200); //2120
  }

}
