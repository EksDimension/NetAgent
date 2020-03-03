package com.eks.netagentdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eks.netagent.core.DownloadListener;
import com.eks.netagent.core.NetAgent;
import com.eks.netagent.core.NetCallbackImpl;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends PermissionActivity {
    private String[] permissionArray = new String[]{Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE};

    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDownload = findViewById(R.id.btnDownload);
        requestRuntimePermission(permissionArray, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {
            }
        });


    }

    public void get(View view) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("11111111", "222222222222");
        headers.put("333", "444444444");
        NetAgent.INSTANCE.setHeaders(headers);
        NetAgent.INSTANCE.addHeader("5555", "666666666666");
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

    public void download(View view) {
        String savePath = getProjectMainFolder() + "/ZykgBaseLib.zip";
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onDownloadFailed(@NotNull String errMsg) {
//                System.out.println("下载失败:"+errMsg);
                Toast.makeText(MainActivity.this, "下载失败:" + errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSucceed(@NotNull String filePath) {
//                System.out.println("下载完毕 路径:"+filePath);
                Toast.makeText(MainActivity.this, "下载完毕 路径:" + filePath, Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgress(long totalSize, long downSize) {

//                System.out.println("总大小:" + totalSize + " 已下载:" + downSize);
                btnDownload.setText(( Double.parseDouble(String.valueOf(downSize)) / Double.parseDouble(String.valueOf(totalSize))) * 100 + "%");
//                System.out.println(downSize+" "+Thread.currentThread().getName());
            }
        };
        NetAgent.INSTANCE.downloadFile("https://static.zysccn.com/zykgsc/upload/img/ZykgBaseLib/ZykgBaseLib.zip", savePath, downloadListener);
    }


    /**
     * 获取项目根目录
     */
    public File getProjectMainFolder() {
        if (getSdcardPath() == null) {
            return null;
        } else {
            File f = new File(getSdcardPath() + "/" + "netAgent");
            if (!f.exists()) {
                /* 创建strRingtoneFolder文件夹 */
                if (f.mkdirs()) {
                    return f;
                } else {
                    return null;
                }
            } else {
                return f;
            }
        }
    }

    /**
     * 判断SdCard是否存在并且是可用的
     * <p>
     * 存储卡的路径 android.os.Environment.getExternalStorageDirectory().getPath();
     *
     * @return
     */
    private String getSdcardPath() {
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                return android.os.Environment.getExternalStorageDirectory()
                        .getPath();
            }
        }
        return null;
    }
}
