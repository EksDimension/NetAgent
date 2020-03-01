package com.eks.netagentdemo;

import android.app.Application;

import com.eks.netagent.core.NetProcessorImpl;
import com.eks.netagent.processors.VolleyProcessor;

/**
 * Created by Riggs on 2020/3/1
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetProcessorImpl.INSTANCE.init(new VolleyProcessor(this));
    }
}
