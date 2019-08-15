package org.ghy.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class UnFairReentrantLockTest {
  private static Lock unfairLock = new ReentrantLock();

  public static void main(String[] args) {
    for (int i = 0; i < 5; i++) {
      new Thread(new ThreadDemo(i)).start();
    }
  }

  static class ThreadDemo implements Runnable {
    Integer id;

    public ThreadDemo(Integer id) {
      this.id = id;
    }

    @Override
    public void run() {
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (int i = 0; i < 2; i++) {
        unfairLock.lock();
        System.out.println("获得锁的线程：" + id);
        unfairLock.unlock();
      }
    }
  }
}
