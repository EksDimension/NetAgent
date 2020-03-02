package com.eks.netagentdemo;

import android.app.Application;

import com.eks.netagent.core.NetAgent;
import com.eks.netagent.processors.retrofit.RetrofitProcessor;

/**
 * Created by Riggs on 2020/3/1
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        NetAgent.INSTANCE.init(new VolleyProcessor(this));
        NetAgent.INSTANCE.init(new RetrofitProcessor());
    }
}
