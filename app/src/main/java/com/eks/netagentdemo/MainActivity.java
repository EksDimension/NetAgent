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
import com.eks.netagent.core.RequestType;
import com.eks.netagent.core.UploadListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends PermissionActivity {
    private String[] permissionArray = new String[]{Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE};

    private Button btnDownload;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetAgent.INSTANCE.setBaseUrl("https://apis.juhe.cn");
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("header1", "AAA");
        headers.put("header2", "BBB");
        NetAgent.INSTANCE.setHeaders(headers);
        NetAgent.INSTANCE.addHeader("header3", "CCC");
        NetAgent.INSTANCE.removeHeader("header2");
        btnDownload = findViewById(R.id.btnDownload);
        btnUpload = findViewById(R.id.btnUpload);
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
        String url = "obdcode/query";
        NetAgent.INSTANCE.request(RequestType.GET, null, url, null, null, new NetCallbackImpl<BeanObdCodeQuery>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(BeanObdCodeQuery objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWithQueryMap(View view) {
        String url = "obdcode/query";
        HashMap<String, String> params = new HashMap<>();
        params.put("code", "P2079");
        params.put("key", "66010dabd6cfc61e55c07f68606e91c2");
        NetAgent.INSTANCE.request(RequestType.GET, null, url, params, null, new NetCallbackImpl<BeanObdCodeQuery>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(BeanObdCodeQuery objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btnGetWithHeaderMap(View view) {
        String url = "obdcode/query";
        HashMap<String, String> header = new HashMap<>();
        header.put("header4", "DDD");
        header.put("header5", "EEE");
        NetAgent.INSTANCE.request(RequestType.GET, null, url, null, header, new NetCallbackImpl<BeanObdCodeQuery>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(BeanObdCodeQuery objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWithQueryHeaderMap(View view) {
        String url = "obdcode/query";
        HashMap<String, String> params = new HashMap<>();
        params.put("code", "P2079");
        params.put("key", "66010dabd6cfc61e55c07f68606e91c2");
        HashMap<String, String> header = new HashMap<>();
        header.put("header4", "DDD");
        header.put("header5", "EEE");
        NetAgent.INSTANCE.request(RequestType.GET, null, url, params, header, new NetCallbackImpl<BeanObdCodeQuery>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(BeanObdCodeQuery objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postWithBody(View view) {
        String url = "toutiao/index";
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "shehui");
        params.put("key", "1883b1aa57644c2b775c608520f6cb2a");
        NetAgent.INSTANCE.request(RequestType.POST, "https://v.juhe.cn", url, params, null, new NetCallbackImpl<BeanToutiao>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(BeanToutiao objResult) {
                Toast.makeText(MainActivity.this, objResult.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void download(View view) {
        String savePath = getProjectMainFolder() + "/navicat111_premium_cs_x64.exe";
        NetAgent.INSTANCE.downloadFile("https://static.zysccn.com/zykgsc/upload/img/ZykgBaseLib/navicat111_premium_cs_x64.exe", savePath, new NetCallbackImpl<String>() {
            @Override
            public void onFailed(@NotNull String errMsg) {
                Toast.makeText(MainActivity.this, "下载失败:" + errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSucceed(@Nullable String filePath) {
                Toast.makeText(MainActivity.this, "下载完毕 路径:" + filePath, Toast.LENGTH_SHORT).show();
            }
        }, new DownloadListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgress(long totalSize, long downSize) {
                btnDownload.setText(new DecimalFormat("0.00").format((Double.parseDouble(String.valueOf(downSize)) / Double.parseDouble(String.valueOf(totalSize))) * 100) + "%");
            }
        });
    }

    public void upload(View view) {
        String filePath = getProjectMainFolder() + "/navicat111_premium_cs_x86.exe";
        HashMap<String, File> uploadFileMap = new HashMap<>();
        uploadFileMap.put("myFile", new File(filePath));
        HashMap<String, String> params = new HashMap<>();
        params.put("methods", "DIY");
        params.put("paths", "ZykgBaseLib/");
        NetAgent.INSTANCE.uploadFile("https://tool.zysccn.com/api/upload/unified", uploadFileMap, params, new NetCallbackImpl<BeanBase<BeanUploadFiles>>() {
                    @Override
                    public void onFailed(@NotNull String errMsg) {
                        Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSucceed(@Nullable BeanBase<BeanUploadFiles> beanUploadFilesBeanBase) {
                        Toast.makeText(MainActivity.this, "上传完毕 路径:" + beanUploadFilesBeanBase.getData().getPath(), Toast.LENGTH_SHORT).show();
                    }
                }
                , new UploadListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgress(long totalSize, long uploadedSize) {
                        btnUpload.setText(new DecimalFormat("0.00").format((Double.parseDouble(String.valueOf(uploadedSize)) / Double.parseDouble(String.valueOf(totalSize))) * 100) + "%");
                    }
                }
        );
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
