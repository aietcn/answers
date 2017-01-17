package self.data.format;

import com.google.flatbuffers.FlatBufferBuilder;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import org.junit.Test;
import self.data.format.flatbuffers.FlatbuffersData;
import self.data.format.flatbuffers.Sensor;
import self.data.format.flatbuffers.SensorData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static self.data.format.flatbuffers.Type.UPLOAD;

/**
 * @author aiet
 */
public class FlatbuffersTest {

  @Test
  public void testSerializeSize() throws Exception {
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
    int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
    int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
    int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
    int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
    int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
    int uid = builder.createString("guava");
    FlatbuffersData.startFlatbuffersData(builder);
    FlatbuffersData.addUid(builder, uid);
    FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
    FlatbuffersData.addType(builder, UPLOAD);
    FlatbuffersData.addList(builder, listVector);
    int offset = FlatbuffersData.endFlatbuffersData(builder);
    FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
    byte[] bytes = builder.sizedByteArray();
    System.out.println(bytes.length);
    assertTrue(bytes.length == 192);
  }

  @Test
  public void testSerializeTime() throws Exception {
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
        int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
        int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
        int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
        int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
        int uid = builder.createString("guava");
        FlatbuffersData.startFlatbuffersData(builder);
        FlatbuffersData.addUid(builder, uid);
        FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
        FlatbuffersData.addType(builder, UPLOAD);
        FlatbuffersData.addList(builder, listVector);
        int offset = FlatbuffersData.endFlatbuffersData(builder);
        FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
        byte[] bytes = builder.sizedByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 700 && avgtime < 800);
  }

  @Test
  public void testSerializeTimePrebuilt() throws Exception {
    /* pre-allocated buffers does make things better */
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      ByteBuffer buffer = ByteBuffer.allocate(200);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatBufferBuilder builder = new FlatBufferBuilder(buffer);
        int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
        int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
        int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
        int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
        int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
        int uid = builder.createString("guava");
        FlatbuffersData.startFlatbuffersData(builder);
        FlatbuffersData.addUid(builder, uid);
        FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
        FlatbuffersData.addType(builder, UPLOAD);
        FlatbuffersData.addList(builder, listVector);
        int offset = FlatbuffersData.endFlatbuffersData(builder);
        FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
        byte[] bytes = builder.sizedByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 600 && avgtime < 700);
  }

  @Test
  public void testSerializeWithCompressionTime() throws Exception {
    /* compression could take a long time */
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      ByteBuffer buffer = ByteBuffer.allocate(200);
      LZ4Factory factory = LZ4Factory.fastestInstance();
      LZ4Compressor compressor = factory.fastCompressor();
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatBufferBuilder builder = new FlatBufferBuilder(buffer);
        int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
        int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
        int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
        int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
        int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
        int uid = builder.createString("guava");
        FlatbuffersData.startFlatbuffersData(builder);
        FlatbuffersData.addUid(builder, uid);
        FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
        FlatbuffersData.addType(builder, UPLOAD);
        FlatbuffersData.addList(builder, listVector);
        int offset = FlatbuffersData.endFlatbuffersData(builder);
        FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
        byte[] bytes = builder.sizedByteArray();
        int maxCompressedLength = compressor.maxCompressedLength(bytes.length);
        byte[] compressed = new byte[maxCompressedLength];
        int compressedLength = compressor.compress(bytes, 0, bytes.length, compressed, 0, maxCompressedLength);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 1700 && avgtime < 1800);
  }

  @Test
  public void testDeserializationGetType() throws Exception {
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
    int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
    int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
    int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
    int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
    int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
    int uid = builder.createString("guava");
    FlatbuffersData.startFlatbuffersData(builder);
    FlatbuffersData.addUid(builder, uid);
    FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
    FlatbuffersData.addType(builder, UPLOAD);
    FlatbuffersData.addList(builder, listVector);
    int offset = FlatbuffersData.endFlatbuffersData(builder);
    FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
    byte[] bytes = builder.sizedByteArray();

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {

      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatbuffersData data = FlatbuffersData.getRootAsFlatbuffersData(ByteBuffer.wrap(bytes));
        data.type();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>43&&avgtime<45);
  }

  @Test
  public void testDeserializationGetListData() throws Exception {
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int xVector = SensorData.createXVector(builder, new double[]{1.1, 4.4});
    int yVector = SensorData.createYVector(builder, new double[]{2.2, 5.5});
    int zVector = SensorData.createZVector(builder, new double[]{3.3, 6.6});
    int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis()});
    int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
    int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});
    int uid = builder.createString("guava");
    FlatbuffersData.startFlatbuffersData(builder);
    FlatbuffersData.addUid(builder, uid);
    FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
    FlatbuffersData.addType(builder, UPLOAD);
    FlatbuffersData.addList(builder, listVector);
    int offset = FlatbuffersData.endFlatbuffersData(builder);
    FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
    byte[] bytes = builder.sizedByteArray();

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {

      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatbuffersData data = FlatbuffersData.getRootAsFlatbuffersData(ByteBuffer.wrap(bytes));
        data.list(0).x(0);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>75&&avgtime<85);
  }

  private byte[] largeData(){
    FlatBufferBuilder builder = new FlatBufferBuilder();
    int xVector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
    int yVector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
    int zVector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
    int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis()});
    int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
    int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});

    int x2Vector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
    int y2Vector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
    int z2Vector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
    int t2Vector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
        System.currentTimeMillis(), System.currentTimeMillis()});
    int dataStreamOffset2 = SensorData.createSensorData(builder, t2Vector, x2Vector, y2Vector, z2Vector, Sensor.MAGNETISM);
    int listVector2 = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset2});

    int uid = builder.createString("guava");
    FlatbuffersData.startFlatbuffersData(builder);
    FlatbuffersData.addUid(builder, uid);
    FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
    FlatbuffersData.addType(builder, UPLOAD);
    FlatbuffersData.addList(builder, listVector);
    FlatbuffersData.addList(builder, listVector2);
    int offset = FlatbuffersData.endFlatbuffersData(builder);
    FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
    byte[] bytes = builder.sizedByteArray();
    return bytes;
  }

  @Test
  public void testSerializeSizeLargeData() throws Exception {
    byte[] bytes = largeData();
    System.out.println(bytes.length);
    assertTrue(bytes.length == 920);
  }

  @Test
  public void testSerializeTimeLargeData() throws Exception {
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatBufferBuilder builder = new FlatBufferBuilder();
        int xVector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
        int yVector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
        int zVector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
        int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
        int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});

        int x2Vector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
        int y2Vector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
        int z2Vector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
        int t2Vector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset2 = SensorData.createSensorData(builder, t2Vector, x2Vector, y2Vector, z2Vector, Sensor.MAGNETISM);
        int listVector2 = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset2});

        int uid = builder.createString("guava");
        FlatbuffersData.startFlatbuffersData(builder);
        FlatbuffersData.addUid(builder, uid);
        FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
        FlatbuffersData.addType(builder, UPLOAD);
        FlatbuffersData.addList(builder, listVector);
        FlatbuffersData.addList(builder, listVector2);
        int offset = FlatbuffersData.endFlatbuffersData(builder);
        FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
        byte[] bytes = builder.sizedByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 2400 && avgtime < 2500); //2450
  }

  @Test
  public void testSerializeTimePrebuiltLargeData() throws Exception {
    /* pre-allocated buffers does make things better */
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatBufferBuilder builder = new FlatBufferBuilder(buffer);
        int xVector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
        int yVector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
        int zVector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
        int tVector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset = SensorData.createSensorData(builder, tVector, xVector, yVector, zVector, Sensor.ACCELERATION);
        int listVector = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset});

        int x2Vector = SensorData.createXVector(builder, new double[]{0.0, 3.3, 6.6, 9.9, 12.12, 15.15, 18.18, 21.21, 24.24, 27.27, 30.30});
        int y2Vector = SensorData.createYVector(builder, new double[]{1.1, 4.4, 7.7, 10.10, 13.13, 16.16, 19.19, 22.22, 25.25, 28.28, 31.31});
        int z2Vector = SensorData.createZVector(builder, new double[]{2.2, 5.5, 8.8, 11.11, 14.14, 17.17, 20.20, 23.23, 26.26, 29.29, 32.32});
        int t2Vector = SensorData.createTimestampVector(builder, new long[]{System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(),
            System.currentTimeMillis(), System.currentTimeMillis()});
        int dataStreamOffset2 = SensorData.createSensorData(builder, t2Vector, x2Vector, y2Vector, z2Vector, Sensor.MAGNETISM);
        int listVector2 = FlatbuffersData.createListVector(builder, new int[]{dataStreamOffset2});

        int uid = builder.createString("guava");
        FlatbuffersData.startFlatbuffersData(builder);
        FlatbuffersData.addUid(builder, uid);
        FlatbuffersData.addTimestamp(builder, System.currentTimeMillis());
        FlatbuffersData.addType(builder, UPLOAD);
        FlatbuffersData.addList(builder, listVector);
        FlatbuffersData.addList(builder, listVector2);
        int offset = FlatbuffersData.endFlatbuffersData(builder);
        FlatbuffersData.finishFlatbuffersDataBuffer(builder, offset);
        byte[] bytes = builder.sizedByteArray();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime > 2250 && avgtime < 2400); //2320
  }

  @Test
  public void testDeserializationGetTypeLargeData() throws Exception {
    byte[] bytes = largeData();
    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {
      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatbuffersData data = FlatbuffersData.getRootAsFlatbuffersData(ByteBuffer.wrap(bytes));
        data.type();
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>43&&avgtime<45); //44
  }

  @Test
  public void testDeserializationGetListDataLargeData() throws Exception {
    byte[] bytes = largeData();

    List<Double> avgs = new ArrayList<>(700);
    for (int j = 0; j < 1000; j++) {

      List<Long> times = new ArrayList<>(10000);
      for (int i = 0; i < 10000; i++) {
        long before = System.nanoTime();
        FlatbuffersData data = FlatbuffersData.getRootAsFlatbuffersData(ByteBuffer.wrap(bytes));
        data.list(0).x(0);
        times.add(System.nanoTime() - before);
      }
      double avg = times.stream().mapToLong(Long::longValue).average().getAsDouble();
      if (j > 300) avgs.add(avg);
    }
    double avgtime = avgs.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    System.out.println(avgtime);
    assertTrue(avgtime>75&&avgtime<80); //76
  }

}
