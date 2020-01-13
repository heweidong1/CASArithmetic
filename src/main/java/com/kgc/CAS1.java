package com.kgc;

import java.util.concurrent.atomic.AtomicInteger;

public class CAS1
{
    private static volatile int m=0;
    private static AtomicInteger atomicI = new AtomicInteger(0);
    public static void increase1()
    {
        m++;
    }

    public static void increase2()
    {
        //先加1 在返回
        atomicI.incrementAndGet();
    }

   static Thread[] ts = new Thread[20];

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++)
        {
            ts[i]= new Thread(new Runnable() {
                @Override
                public void run() {
                    CAS1.increase2();
                }
            });
            ts[i].start();
            //等待线程结束，在往下进行
            ts[i].join();

        }
        System.out.println(atomicI.get());
    }
}
