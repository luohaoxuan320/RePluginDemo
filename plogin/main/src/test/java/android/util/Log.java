package android.util;

public class Log {
  public static int i(String tag, String msg) {
    System.out.println("TAG=" + tag + "  msg=" + msg);
    return 0;
  }
}
