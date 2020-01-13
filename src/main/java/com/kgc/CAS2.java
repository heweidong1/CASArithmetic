package com.kgc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CAS2
{
    //会丢失历史值

    private static AtomicInteger atomicI = new AtomicInteger(100);

    //第一个参数为默认值  第二个参数为版本号，版本控制，设置过什么值，可以通过版本号获取到，历史值

    private static AtomicStampedReference asr = new AtomicStampedReference(100,1);

    public static void main(final String[] args) throws InterruptedException {
            Thread t1= new Thread(new Runnable() {
                @Override
                public void run() {
                    //比较并且修改，第一个参数为预期值，第二值为修改得值，首先判断实际值是否是100，
                    //如果是就修改为110

                    atomicI.compareAndSet(100,110);
                    System.out.println(atomicI.get());
                }
            });



            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    atomicI.compareAndSet(110,100);
                    System.out.println(atomicI);
                }
            });


            Thread t3  =new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    atomicI.compareAndSet(100,120);
                    System.out.println(atomicI);
                }
            });



        System.out.println("===========================================================");
        Thread tf1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*
                * 第一个参数为预期值，第二个参数为要修改为什么值
                * 第三个参数为版本号，会和上个版本进行对比，
                * 第四个参数为，修得版本号
                * */

                System.out.println("1->"+asr.compareAndSet(100,110,asr.getStamp(),asr.getStamp()+1));
                System.out.println("2->"+asr.compareAndSet(110,100,asr.getStamp(),asr.getStamp()+1));
            }
        });
        tf1.start();


        Thread tsf2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //版本号
                int stamp = asr.getStamp();
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("3->"+asr.compareAndSet(100,120,asr.getStamp(),asr.getStamp()+1));

            }
        });
        tsf2.start();

    }







}
