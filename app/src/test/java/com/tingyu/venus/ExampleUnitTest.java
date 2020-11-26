package com.tingyu.venus;

import com.tingyu.venus.utils.threadpool.SingleThreadPoolExecutor;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }




    @Test
    public void TestMain() throws IOException {


        SingleThreadPoolExecutor singleThreadPoolExecutor = SingleThreadPoolExecutor.newInstance();
        for (int i = 0; i <10 ; i++) {
        singleThreadPoolExecutor.execute(()->{


                System.out.println(Thread.currentThread().getName()+"\t 线程池执行任务开始");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t 线程池执行任务开始");



        });
        }

      //  coreThreadPool.shutdown();

        if(singleThreadPoolExecutor.isShutdown()){
            System.out.println("线程池已经关闭");
        }else{
            System.out.println("线程池未关闭");
        }


        SingleThreadPoolExecutor singleThreadPoolExecutor2 = SingleThreadPoolExecutor.newInstance();
        for (int i = 0; i <10 ; i++) {
            singleThreadPoolExecutor2.execute(()->{


                System.out.println(Thread.currentThread().getName()+"\t 线程池执行任务开始");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t 线程池执行任务开始");



            });
        }


        System.in.read();

        System.out.println("============="+(8<<1));
    }
}