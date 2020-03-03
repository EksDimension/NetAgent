package com.eks.netagentdemo

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by Riggs on 2020/3/4
 */
abstract class PermissionActivity : AppCompatActivity() {

    /**
     * 运行时权限监听
     */
    private var mPermissionListener: PermissionListener? = null

    /**
     * 运行时权限请求
     *
     * @param permissions 权限数组
     * @param listener    权限监听接口
     */
    fun requestRuntimePermission(permissions: Array<String>, listener: PermissionListener) {
        mPermissionListener = listener
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPermissionListener?.onGranted()
        } else {
            val permissionList = arrayListOf<String>()
            permissions.forEach { if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) permissionList.add(it) }
            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionList.toArray(arrayOfNulls(permissionList.size)), 1)
            } else {
                //授权成功
                mPermissionListener?.onGranted()
            }
        }
    }

    /**
     * 运行时权限请求后回调
     *
     * @param requestCode  请求码
     * @param permissions  权限数组
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                //有权限被拒绝
                if (grantResults.isNotEmpty()) {
                    val deniedPermissions = arrayListOf<String>()
                    grantResults.forEachIndexed { index, grantResult ->
                        val permission = permissions[index]
                        if (grantResult != PackageManager.PERMISSION_GRANTED) deniedPermissions.add(permission)
                    }
                    //全部授权成功
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener?.onGranted()
                    } else {
                        mPermissionListener?.onDenied(deniedPermissions)
                    }
                }
            }
        }
    }
}