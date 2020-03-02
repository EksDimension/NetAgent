package com.eks.netagentdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eks.netagent.core.NetCallbackImpl;
import com.eks.netagent.core.NetAgent;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void get(View view) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("11111111","222222222222");
        headers.put("333","444444444");
        NetAgent.INSTANCE.setHeaders(headers);
        NetAgent.INSTANCE.addHeader("5555","666666666666");
        NetAgent.INSTANCE.removeHeader("333");
        String baseUrl = "https://apis.juhe.cn";
        String url = "/obdcode/query";
        HashMap<String, Object> params = new HashMap<>();
        params.put("code", "P2079");
        params.put("key", "66010dabd6cfc61e55c07f68606e91c2");
        NetAgent.INSTANCE.get(baseUrl, url, params, new NetCallbackImpl<BeanObdCodeQuery>() {
            @Override
            public void onSucceed(BeanObdCodeQuery objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void post(View view) {
        String baseUrl = "https://v.juhe.cn";
        String url = "/toutiao/index";
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", "shehui");
        params.put("key", "1883b1aa57644c2b775c608520f6cb2a");
        NetAgent.INSTANCE.post(baseUrl, url, params, new NetCallbackImpl<BeanToutiao>() {
            @Override
            public void onSucceed(BeanToutiao objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
