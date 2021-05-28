package com.ibirdman.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.ibirdman.aidldemo.impl.IAdvertManager;
import com.ibirdman.aidldemo.model.Advert;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.Nullable;

public class AdvertManagerService extends Service {

    private CopyOnWriteArrayList mAdvertList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IAdvertManager.Stub() {
        @Override
        public List getAdvertList() throws RemoteException {
            return mAdvertList;
        }

        @Override
        public void addAdvert(Advert ad) throws RemoteException {
            mAdvertList.add(ad);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
