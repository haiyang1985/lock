package org.ghy.lock.synchronize;

/**
 * @author hy_gu on 2019/8/15
 **/
public class SynchronizedTest {

  public static void main(String[] args) {

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          method1();

          if (1 != 1) {
            break;
          }
        }
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          method2();

          if (1 != 1) {
            break;
          }
        }
      }
    }).start();
  }

  private static synchronized void method1() {
    System.out.println(Thread.currentThread().getName() + ",method1");
    method2();
  }

  private static synchronized void method2() {
    System.out.println(Thread.currentThread().getName() + ",method2");
  }
}
