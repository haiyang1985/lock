package org.ghy.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class DeadLockTimeout {
  static Lock lock1 = new ReentrantLock();
  static Lock lock2 = new ReentrantLock();

  /**
   * 线程通过调用tryLock()方法获取锁,第一次获取锁失败时会休眠10毫秒,然后重新获取，直到获取成功。<br/>
   * 第二次获取失败时,首先会释放第一把锁,再休眠10毫秒,然后重试直到成功为止。<br/>
   * 线程获取第二把锁失败时将会释放第一把锁，这是解决死锁问题的关键,避免了两个线程分别持有一把锁然后相互请求另一把锁。<br/>
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("lock1 hashCode:" + lock1.hashCode());
    System.out.println("lock2 hashCode:" + lock2.hashCode());

    Thread thread = new Thread(new ThreadDemo(lock1, lock2));// 该线程先获取锁1,再获取锁2
    Thread thread1 = new Thread(new ThreadDemo(lock2, lock1));// 该线程先获取锁2,再获取锁1
    thread.start();
    thread1.start();
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
        while (!firstLock.tryLock()) {
          System.out.println("我是线程：" + Thread.currentThread().getName() + "获取不到" + lock1.hashCode() + "等待10ms");
          TimeUnit.MILLISECONDS.sleep(10);
        }
        System.out.println("我是线程：" + Thread.currentThread().getName() + "已锁定:" + lock1.hashCode());

        while (!secondLock.tryLock()) {
          System.out.println("我是线程：" + Thread.currentThread().getName() + "获取不到" + lock2.hashCode() + "等待10ms");
          firstLock.unlock();
          TimeUnit.MILLISECONDS.sleep(10);
        }
        System.out.println("我是线程：" + Thread.currentThread().getName() + "已锁定:" + lock2.hashCode());
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
