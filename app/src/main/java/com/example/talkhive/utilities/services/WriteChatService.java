package com.example.talkhive.utilities.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.talkhive.utilities.firebaseutils.FirebaseChatUtils;
import com.example.talkhive.utilities.model.MessageModel;

public class WriteChatService extends Service {
    private Looper serviceLooper;
    private WriteChatService.ServiceHandler serviceHandler;
    private static final String SERVICE_CLASS_TAG = WriteChatService.class.getName().toString();
    private static MessageModel modelObj;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i(SERVICE_CLASS_TAG, "Firebase user updation thread");
            FirebaseChatUtils.writeMessage(modelObj);
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        modelObj = (MessageModel) intent.getSerializableExtra("Message");
        Log.i(SERVICE_CLASS_TAG,"HI");
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
