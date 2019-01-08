package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片压缩工具类
 */
public class ImageCompressUtil {

    private static List<String> mImageList = new ArrayList<>();// 临时图片集合
    private static String mImagePath = ""; // 单个临时图片
    public static String cachePath = "";
    //    public static int reqWidth = 320;
//    public static int reqHeight = 480;
    public static int reqWidth = 320;
    public static int reqHeight = 480;
//    public static int reqWidth = 2048;
//    public static int reqHeight = 2048;

    //压缩单张图片方法
    public static void compressImage(final Context context, final String filePath, final ProcessImgCallBack callBack) {
        mImagePath = "";//清空路径
        //如果路径是图片，则进行压缩
        if (isImage(filePath)) {
            mImagePath = compress(context, filePath);
        }
        callBack.compressSuccess(mImagePath);
    }

    //图片压缩的方法
    public static String compress(Context context, String filePath) {

        if (TextUtils.isEmpty(filePath))
            return filePath;

        File file = new File(filePath);
        if (!file.exists())//判断路径是否存在
            return filePath;

        if (file.length() < 1)//文件是否为空
            return null;

        File tempFile = getDiskCacheDir(context);
        String outImagePath = tempFile.getAbsolutePath(); // 输出图片文件路径


        int degree = ImageRotateUtil.getBitmapDegree(filePath); // 检查图片的旋转角度

        Bitmap bitmap = GoogleCompress(filePath);

        // 旋转:这步处理主要是为了处理三星手机拍的照片
        if (degree > 0) {
            bitmap = ImageRotateUtil.rotateBitmapByDegree(bitmap, degree);
        }

        // 写入文件
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }

        return outImagePath;
    }

    public static Bitmap GoogleCompress(String path) {
        //谷歌官网压缩图片
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 计算压缩比例值
     * 按照2、3、4...倍压缩
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int picheight = options.outHeight;
        final int picwidth = options.outWidth;
        Log.i("--->", "原尺寸:" + picwidth + "*" + picheight);

        int targetheight = picheight;
        int targetwidth = picwidth;
        int inSampleSize = 1;

        if (targetheight > reqHeight || targetwidth > reqWidth) {
            while (targetheight >= reqHeight && targetwidth >= reqWidth) {
                inSampleSize += 1;
                targetheight = picheight / inSampleSize;
                targetwidth = picwidth / inSampleSize;
            }
        }

        Log.i("--->", "最终压缩比例:" + inSampleSize + "倍/新尺寸:" + targetwidth + "*" + targetheight);
        return inSampleSize;
    }

    //单张图片压缩成功后的回调接口
    public interface ProcessImgCallBack {
        void compressSuccess(String imgPath);
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return 文件后缀名
     */
    public static String getFileType(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            int typeIndex = fileName.lastIndexOf(".");
            if (typeIndex != -1) {
                String fileType = fileName.substring(typeIndex + 1).toLowerCase();
                return fileType;
            }
        }
        return "";
    }

    /**
     * 判断是否是图片
     *
     * @param fileName
     * @return 是否是图片类型
     */
    public static boolean isImage(String fileName) {
        String type = getFileType(fileName);
        if (!TextUtils.isEmpty(type) && (type.equals("jpg") || type.equals("gif")
                || type.equals("png") || type.equals("jpeg")
                || type.equals("bmp") || type.equals("wbmp")
                || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }

    /**
     * 将压缩后的图片存储在缓存中
     */
    public static File getDiskCacheDir(Context ctx) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = ctx.getExternalCacheDir().getPath();
        } else {
            cachePath = ctx.getCacheDir().getPath();
        }
        String uniqueName = System.currentTimeMillis() + "_tmp.jpg";
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 清理缓存文件夹
     */
    public static void clearCache(Context ctx) {
        File file = new File(cachePath);
        File[] childFile = file.listFiles();
        if (childFile == null || childFile.length == 0) {
            return;
        }
        for (File f : childFile) {
            f.delete(); // 循环删除子文件
        }
    }

    /**
     * 从图片路径读取出图片
     *
     * @param imagePath
     * @return
     */
    private Bitmap decodeFile(String imagePath) {
        Bitmap bitmap = null;
        try {
            File file = new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 根据图片进行压缩
     * 可根据比例   可根据指定像素
     * @param bmp
     * @param file
     */
    public static void compressBitmapToFile(Bitmap bmp, File file) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 5;
        // 压缩Bitmap到对应尺寸
        int with = bmp.getWidth();
        int height = bmp.getHeight();
        if (with > height) {
            with = 1280;
            height = 768;
        } else {
            with = 768;
            height = 1280;
        }

//        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Bitmap result = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
//        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        Rect rect = new Rect(0, 0, with, height);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
