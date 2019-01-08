package utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class SDUtils {
    //SD卡地址
    public static String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
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


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    //刷新SD卡
    public static void refreshAlbum(Context context, File file) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }


}
