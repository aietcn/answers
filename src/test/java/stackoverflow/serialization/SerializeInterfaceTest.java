package stackoverflow.serialization;

import org.junit.Test;

import java.io.*;

/**
 * @author aiet
 * @see <a href="http://stackoverflow.com/questions/41590091/serialize-an-interface/41591395#answer-41591395">Serialize an Interface</a>
 */
public class SerializeInterfaceTest {

  @Test
  public void testSerializeInterface() throws Exception {
    FileOutputStream fileOutputStream = new FileOutputStream("interfaceSerialize.bin");
    ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
    MyCallback tout = new MyCallbackImpl();
    System.out.println(tout.toString());
    out.writeObject(tout);
    MyObj obj = new MyObj("test", tout);
    out.writeObject(obj);
    System.out.println(obj.getCallback());
    out.close();

    FileInputStream fileInputStream = new FileInputStream("interfaceSerialize.bin");
    ObjectInputStream in = new ObjectInputStream(fileInputStream);
    MyCallback tin = (MyCallback) in.readObject();
    System.out.println(tin.toString());

  }


  public static class MyObj implements Serializable {
    private static final long serialVersionUID = -29238982928391L;

    private String text;
    private MyCallback callback;

    MyObj(String text, MyCallback callback) {
      this.text = text;
      this.callback = callback;
    }

    MyCallback getCallback() {
      return callback;
    }

  }

  interface MyCallback extends Serializable {
  }

  static class MyCallbackImpl implements MyCallback {
    private static final long serialVersionUID = -19238982928391L;

  }
}
