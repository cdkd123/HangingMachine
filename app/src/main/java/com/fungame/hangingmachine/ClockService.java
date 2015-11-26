package com.fungame.hangingmachine;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by admin on 15/11/26.
 */
public class ClockService extends Service {

    boolean start = true;
    private int guaji = 0;
    private Handler mHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        startTick();
        return new ServiceBinder();
    }

    //此方法是为了可以在Acitity中获得服务的实例
    public class ServiceBinder extends Binder {
        public ClockService getService() {
            return ClockService.this;
        }
    }


    private void startTick() {
        start = true;

        System.out.println("tom--> start Tick!");
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(start){
                    guaji ++;
                    System.out.println("guaji ---");
                    try {
                        Thread.sleep(1000);
                        if(mHandler != null){
                            mHandler.sendEmptyMessage(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end----");
            }

        }.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        start = false;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}
