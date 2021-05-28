package com.ibirdman.aidldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ibirdman.aidldemo.impl.IAdvertManager;
import com.ibirdman.aidldemo.model.Advert;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private IAdvertManager mAdvertManager;

    private TextView mInfo;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                service.linkToDeath(mDeathReciept, 0);
                //这里将binder对象转换为aidl对象，从而能够调用aidl方法。
                IAdvertManager iAdvertManager = IAdvertManager.Stub.asInterface(service);
                mAdvertManager = iAdvertManager;
                List advertList = mAdvertManager.getAdvertList();

                Log.e(TAG, JSON.toJSONString(advertList));
                Advert advert = new Advert("one", 23, "bird");
                mAdvertManager.addAdvert(advert);
                Log.e(TAG, JSON.toJSONString(iAdvertManager.getAdvertList()));
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private IBinder.DeathRecipient mDeathReciept = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mAdvertManager == null) {
                return;
            }
            mAdvertManager.asBinder().unlinkToDeath(mDeathReciept, 0);
            mAdvertManager = null;
            /*重新绑定远程*/
            bind();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfo = findViewById(R.id.info);

        bind();
    }

    private void bind() {
        Intent intent = new Intent(this, AdvertManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    int index = 0;

    public void onTest1(View view) {
        index++;
        Advert advert = new Advert(index + "", index, index + "");
        try {
            mAdvertManager.addAdvert(advert);
            mInfo.setText(JSON.toJSONString(mAdvertManager));
            Log.d(TAG, "test:" + JSON.toJSONString(mAdvertManager));
        } catch (RemoteException e) {
            Log.e(TAG, "test: " + e.toString());
        }
    }
}
