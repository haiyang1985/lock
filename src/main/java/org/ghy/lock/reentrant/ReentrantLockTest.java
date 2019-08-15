package org.ghy.lock.reentrant;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class ReentrantLockTest {

  // https://www.cnblogs.com/takumicx/p/9338983.html
  public static void main(String[] args) {
    ReentrantLock lock = new ReentrantLock();
    for (int i = 0; i < 3; i++) {
      lock.lock();
    }

    for (int i = 1; i <= 3; i++) {
      try {

      } finally {
        lock.unlock();
      }
    }
  }


}
