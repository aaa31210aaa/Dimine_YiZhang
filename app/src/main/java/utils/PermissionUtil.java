package utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;

public class PermissionUtil {
    //读写文件权限
    public static String WriteFilePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //创建删除文件权限
    public static String CreateDeleteFilePermission = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;
    //定位权限
    public static String LocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
    //调用相机权限
    public static String CameraPermission = Manifest.permission.CAMERA;
    //网络权限
    public static String InternetPermission = Manifest.permission.INTERNET;
    //读写文件权限响应码
    public static int STORAGE_REQUESTCODE = 100;
    //创建删除文件权限响应码
    public static int FILESYSTEMS_REQUESTCODE = 101;
    //相机权限响应码
    public static int CAMERA_REQUESTCODE = 102;
    //地图权限响应码
    public static int LOCATION_REQUESTCODE = 103;
    //网络权限响应码
    public static int INTERNET_REQUESTCODE = 104;
    //文件删除权限响应码
    public static int CREATEDELETEFILE_REQUESTCODE = 105;


    //相机询问权限
    public static void CameraPermission(Fragment fragment) {
        AndPermission.with(fragment)
                .requestCode(CAMERA_REQUESTCODE)
                .permission(CameraPermission)
                .send();
    }

    //相机询问权限
    public static void CameraPermission(Activity activity) {
        AndPermission.with(activity)
                .requestCode(CAMERA_REQUESTCODE)
                .permission(CameraPermission)
                .send();
    }


    //相册询问权限
    public static void GalleryPermission(Fragment fragment) {
        AndPermission.with(fragment)
                .requestCode(STORAGE_REQUESTCODE)
                .permission(WriteFilePermission)
                .send();
    }

    //文件权限
    public static void FileRWPermission(Activity activity) {
        AndPermission.with(activity)
                .requestCode(STORAGE_REQUESTCODE)
                .permission(WriteFilePermission)
                .send();
    }

    public static void FileCdFilePermission(Context context) {
        AndPermission.with((Activity) context)
                .requestCode(CREATEDELETEFILE_REQUESTCODE)
                .permission(CreateDeleteFilePermission)
                .send();
    }

    //地图权限
    public static void LocationPermission(Activity activity) {
        AndPermission.with(activity)
                .requestCode(LOCATION_REQUESTCODE)
                .permission(LocationPermission)
                .send();
    }




}
