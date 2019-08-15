package org.ghy.lock.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class ConditionTest {
  static ReentrantLock lock = new ReentrantLock();
  static Condition condition = lock.newCondition();

  public static void main(String[] args) throws InterruptedException {
    lock.lock();
    new Thread(new SignalThread()).start();
    System.out.println("主线程等待通知");
    try {
      condition.await();
    } finally {
      lock.unlock();
    }
    System.out.println("主线程恢复运行");
  }

  static class SignalThread implements Runnable {

    @Override
    public void run() {
      lock.lock();
      try {
        condition.signal();
        System.out.println("子线程通知");
      } finally {
        lock.unlock();
      }
    }
  }
}
