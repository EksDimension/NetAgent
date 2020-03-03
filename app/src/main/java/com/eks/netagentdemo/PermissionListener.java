package com.eks.netagentdemo;

import java.util.List;

public interface PermissionListener {
    //授权，同意
    void onGranted();

    //拒绝
    void onDenied(List<String> deniedPermission);
}
