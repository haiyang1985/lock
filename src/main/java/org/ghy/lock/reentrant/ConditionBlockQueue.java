package org.ghy.lock.reentrant;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hy_gu on 2019/8/15
 **/
public class ConditionBlockQueue {
  public static void main(String[] args) {
    final BlockQueue blockQueue = new BlockQueue<>(2);
    for (int i = 0; i < 10; i++) {
      final int data = i;
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            blockQueue.enqueue(data);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }

    for (int i = 0; i < 10; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            blockQueue.dequeue();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }

  public static class BlockQueue<E> {
    int size;
    ReentrantLock lock = new ReentrantLock();
    LinkedList<E> list = new LinkedList();
    Condition notFull = lock.newCondition();// 队列满时的等待条件
    Condition notEmpty = lock.newCondition();// 队列空时的等待条件

    public BlockQueue(int size) {
      this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
      lock.lock();
      try {
        while (list.size() == size) {
          // 队列已满,在notFull条件上等待
          notFull.await();
        }
        // 入队:加入链表末尾
        list.add(e);
        System.out.println("入队：" + e);
        // 通知在notEmpty条件上等待的线程
        notEmpty.signal();
      } finally {
        lock.unlock();
      }
    }

    public E dequeue() throws InterruptedException {
      E e;
      lock.lock();
      try {
        while (list.isEmpty()) {
          // 队列为空,在notEmpty条件上等待
          notEmpty.await();
        }
        // 出队:移除链表首元素
        e = list.removeFirst();
        System.out.println("出队：" + e);
        // 通知在notFull条件上等待的线程
        notFull.signal();
        return e;
      } finally {
        lock.unlock();
      }
    }
  }
}
