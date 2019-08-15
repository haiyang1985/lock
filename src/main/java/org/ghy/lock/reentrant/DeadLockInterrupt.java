package org.ghy.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class DeadLockInterrupt {
  static Lock lock1 = new ReentrantLock();
  static Lock lock2 = new ReentrantLock();

  /**
   * 如果不设置thread1.interrupt，那么两个线程互相持有对方的锁，所以导致了死锁<br/>
   * 如果thread1.interrupt，那么thread1就会被中断，ReentrantLock可以响应线程中断，这个时候thread2就可以执行下去了。
   */
  public static void main(String[] args) {
    System.out.println("firstLock hashCode:" + lock1.hashCode());
    System.out.println("secondLock hashCode:" + lock2.hashCode());

    Thread thread1 = new Thread(new ThreadDemo(lock1, lock2));
    Thread thread2 = new Thread(new ThreadDemo(lock2, lock1));
    thread1.start();
    thread2.start();
    thread1.interrupt();
  }

  static class ThreadDemo implements Runnable {
    Lock firstLock;
    Lock secondLock;

    public ThreadDemo(Lock firstLock, Lock secondLock) {
      this.firstLock = firstLock;
      this.secondLock = secondLock;
    }

    @Override
    public void run() {
      try {
        System.out.println("我是线程:" + Thread.currentThread().getName());
        System.out.println("开始锁定:" + firstLock.hashCode());
        firstLock.lockInterruptibly();
        System.out.println("已锁定:" + firstLock.hashCode());
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println("开始锁定:" + secondLock.hashCode());
        secondLock.lockInterruptibly();
        System.out.println("已锁定:" + secondLock.hashCode());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        firstLock.unlock();
        secondLock.unlock();
        System.out.println(Thread.currentThread().getName() + "正常结束!");
      }
    }
  }
}
