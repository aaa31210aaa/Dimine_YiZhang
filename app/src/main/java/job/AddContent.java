package job;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.dimine_yizhang.R;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import adapter.AddContentAdapter;
import utils.BaseActivity;
import utils.CameraUtil;
import utils.ImageCompressUtil;
import utils.SDUtils;
import utils.ShowToast;
import utils.WaterImage;

public class AddContent extends BaseActivity {
    private ImageView job_pj_detail_back;
    //标题
    private TextView add_content_title;
    //提交
    private TextView add_content_submit;
    //详情内容
    private EditText add_content_etv;
    //照片添加
    private MyGridView add_content_image;
    //默认字符
    private static final String myCode = "000000";
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    //返回码
    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private AddContentAdapter gridAdapter;
    //照相名字地址
    private static String IMAGE_FILE_NAME = System.currentTimeMillis() + ".jpg";
    private Uri cameraFileUri;
    private String savePath;
    private File picture;
    //加水印后的图片地址
    private String waterPath;
    private File waterf;
    //当前位置的信息
    private String local;
    //拍照的照片集合
    private ArrayList<String> list;

    //定位需要的声明
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        initView();
        initLocal();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        CameraUtil.init(this);
        job_pj_detail_back = (ImageView) findViewById(R.id.job_pj_detail_back);
        add_content_title = (TextView) findViewById(R.id.add_content_title);
        add_content_submit = (TextView) findViewById(R.id.add_content_submit);
        add_content_etv = (EditText) findViewById(R.id.add_content_etv);
        add_content_image = (MyGridView) findViewById(R.id.add_content_image);
    }

    @Override
    protected void initData() {
        imagePaths = new ArrayList<String>();
        list = new ArrayList<>();
        Intent intent = getIntent();
        add_content_title.setText(intent.getStringExtra("title"));

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        add_content_image.setNumColumns(cols);

        add_content_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if (myCode.equals(imgs)) {
//                    PhotoPickerIntent intent = new PhotoPickerIntent(AddContent.this);
//                    imagePaths.remove(imagePaths.size() - 1);
//                    intent.setSelectModel(SelectModel.MULTI);
//                    intent.setShowCarema(true); // 是否显示拍照
//                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
//                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
//                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    if (add_content_image.getCount() >= 1) {
                        ShowToast.showShort(AddContent.this, "最多添加1张图片");
                    } else {
                        createFile();
                        picture = new File(savePath, System.currentTimeMillis() + ".jpg");
                        cameraFileUri = Uri.fromFile(picture);
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                } else {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(AddContent.this);
                    imagePaths.remove(imagePaths.size() - 1);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add(myCode);
        gridAdapter = new AddContentAdapter(this, imagePaths);
        add_content_image.setAdapter(gridAdapter);
    }

    private void initLocal() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    protected void setOnClick() {
        job_pj_detail_back.setOnClickListener(this);
        add_content_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.job_pj_detail_back:
                finish();
                break;
            case R.id.add_content_submit:
                ShowToast.showShort(AddContent.this, "添加成功");
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
//                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
//                    Log.d(TAG, "list:======= " + "list =--------- " + list.size());
//                    loadAdpater(list);
                    final String imagePath = cameraFileUri.getPath();
                    Log.e("ces", imagePath + "---------------");
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    //获得旋转角度
                    int degree = WaterImage.getBitmapDegree(imagePath);
                    //矫正照片被旋转
                    bitmap = WaterImage.rotateBitmapByDegree(bitmap, degree);
                    //添加水印
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String water_time = "" + sdf.format(new Date());

                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄时间：" + water_time, 50, R.color.yellow_orange, CameraUtil.screenHeight / 20, CameraUtil.screenWidth - 100);
                    bitmap = WaterImage.drawTextToLeftBottom(this, bitmap, "拍摄地点：" + local, 50, R.color.yellow_orange, CameraUtil.screenHeight / 20, CameraUtil.screenWidth - 160);

                    //删除不带水印的图片
                    picture.delete();
                    String path = saveBitmap(bitmap);
                    //保存带水印的图片并添加到集合中展示
                    list.add(path);
                    SDUtils.refreshAlbum(AddContent.this, picture);
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = " + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        } else {
            if (data == null) {
                if (!imagePaths.contains(myCode)) {
                    imagePaths.add(myCode);
                }
                gridAdapter.notifyDataSetChanged();
            } else {
                ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                loadAdpater(ListExtra);
            }
        }
    }

    //选择图片
    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains(myCode)) {
            paths.remove(myCode);
        }
        paths.add(myCode);
        imagePaths.addAll(paths);
        gridAdapter.notifyDataSetChanged();
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存照片
     *
     * @param mBitmap
     * @return
     */
    public String saveBitmap(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            ShowToast.showShort(this, "内存卡异常，请检查内存卡插入是否正确");
            return "";
        }
        //用时间戳生成照片名称
        waterPath = System.currentTimeMillis() + ".jpg";
        waterf = new File(SDUtils.sdPath + "/LEMS/", waterPath);
        createWaterFile();

        ImageCompressUtil.compressBitmapToFile(mBitmap, waterf);
        SDUtils.refreshAlbum(this, waterf);
        return waterf.getAbsolutePath();
    }

    /**
     * 创建加水印后的图片文件夹
     */
    private void createWaterFile() {
        savePath = SDUtils.sdPath + "/LEMS/";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        savePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }

}
