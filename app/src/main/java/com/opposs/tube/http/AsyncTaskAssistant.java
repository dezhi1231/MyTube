package com.opposs.tube.http;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by xcl on 16/2/24.
 */
public class AsyncTaskAssistant {

    //public static final int CORE_POOL_SIZE = 5;

    public static final int KEEP_ALIVE = 5;

    public static final int MAXIMUM_POOL_SIZE = 128;

    public static ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    public static BlockingQueue<Runnable> sPoolWorkQueue;

    static class MyThreadFactory implements ThreadFactory{

        public MyThreadFactory(){

        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "thread_low_version");
        }
    }


   static {


       if (Build.VERSION.SDK_INT < 11 || !(AsyncTask.THREAD_POOL_EXECUTOR instanceof ThreadPoolExecutor)) {

            sPoolWorkQueue = new LinkedBlockingQueue(10);

            THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(KEEP_ALIVE, MAXIMUM_POOL_SIZE, 5, TimeUnit.SECONDS, sPoolWorkQueue, new MyThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());

       }else{

           THREAD_POOL_EXECUTOR = (ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR;

           THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());

           sPoolWorkQueue = THREAD_POOL_EXECUTOR.getQueue();

       }

    }

    public static void executeOnThreadPool(Runnable runnable) {

        THREAD_POOL_EXECUTOR.execute(runnable);

    }


}
