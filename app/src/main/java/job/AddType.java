package job;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.dimine_yizhang.R;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.CommonlyGridViewAdapter;
import okhttp3.Call;
import utils.BaseActivity;
import utils.CameraUtil;
import utils.DateUtils;
import utils.ImageCompressUtil;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.ShowToast;
import utils.WaterImage;

public class AddType extends BaseActivity {
    private ImageView add_type_back;
    //被检查单位
    private TextView add_type_bjcdw;
    //地址
    private EditText add_type_address;
    //法人代表
    private EditText add_type_frdb;
    //职务
    private EditText add_type_zw;
    //检查场所
    private EditText add_type_jccs;
    //检查开始时间
    private TextView add_type_startdate;
    //检查结束时间
    private TextView add_type_enddate;
    //执法行政机关
    private EditText add_type_zfxzjg;
    //主办人员
    private EditText add_type_zbry;
    //主办人员证件号码
    private EditText add_type_zbryzjh;
    //协办人员
    private EditText add_type_xbry;
    //协办人员证件号码
    private EditText add_type_xbryzjh;
    //检查描述
    private EditText add_type_jcms;
    private MyGridView job_add_gridview;


    //默认字符
    private static final String myCode = "000000";
    //选择的图片的集合
    private ArrayList<String> imagePaths;
    private static final int REQUEST_CAMERA_CODE = 10;// 相机
    private static final int REQUEST_PREVIEW_CODE = 20; //预览
    private CommonlyGridViewAdapter gridAdapter;

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


    //是否需要整改
    private RadioGroup add_type_radiogroup1;
    private RadioButton add_type_zgyes;
    private RadioButton add_type_zgno;
    private String sfzg = "是";

    //行政处罚分类
    private RadioGroup add_type_radiogroup2;
    private RadioButton add_type_ybcx;
    private RadioButton add_type_jycx;
    private String cffl = "一般程序";

    private Map<String, String> sfzg_map = new HashMap<>();
    private Map<String, String> cffl_map = new HashMap<>();


    //检查单位
    private EditText add_type_jcdw;

    private boolean first_start = true; //排查日期第一次点击
    private boolean first_end = true; //治理截止日期第一次点击
    //日期
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat formatter;

    private int yearStartDate;
    private int monthStartDate;
    private int dayStartDate;
    private int yearEndDate;
    private int monthEndDate;
    private int dayEndDate;

    private TextView add_type_submit;
    private String qyid = "";

    private String imgs;
    private int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        initView();
        initLocal();
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        add_type_back = (ImageView) findViewById(R.id.add_type_back);
        add_type_bjcdw = (TextView) findViewById(R.id.add_type_bjcdw);
        add_type_address = (EditText) findViewById(R.id.add_type_address);
        add_type_frdb = (EditText) findViewById(R.id.add_type_frdb);
        add_type_zw = (EditText) findViewById(R.id.add_type_zw);
        add_type_jccs = (EditText) findViewById(R.id.add_type_jccs);
        add_type_startdate = (TextView) findViewById(R.id.add_type_startdate);
        add_type_enddate = (TextView) findViewById(R.id.add_type_enddate);
        add_type_zfxzjg = (EditText) findViewById(R.id.add_type_zfxzjg);
        add_type_zbry = (EditText) findViewById(R.id.add_type_zbry);
        add_type_zbryzjh = (EditText) findViewById(R.id.add_type_zbryzjh);
        add_type_xbry = (EditText) findViewById(R.id.add_type_xbry);
        add_type_xbryzjh = (EditText) findViewById(R.id.add_type_xbryzjh);
        add_type_jcms = (EditText) findViewById(R.id.add_type_jcms);
        add_type_radiogroup1 = (RadioGroup) findViewById(R.id.add_type_radiogroup1);
        add_type_zgyes = (RadioButton) findViewById(R.id.add_type_zgyes);
        add_type_zgno = (RadioButton) findViewById(R.id.add_type_zgno);
        add_type_radiogroup2 = (RadioGroup) findViewById(R.id.add_type_radiogroup2);
        add_type_ybcx = (RadioButton) findViewById(R.id.add_type_ybcx);
        add_type_jycx = (RadioButton) findViewById(R.id.add_type_jycx);
        add_type_jcdw = (EditText) findViewById(R.id.add_type_jcdw);
        add_type_submit = (TextView) findViewById(R.id.add_type_submit);
        job_add_gridview = (MyGridView) findViewById(R.id.job_add_gridview);
    }

    @Override
    protected void initData() {
        //设置初始日期
        String nowdate = DateUtils.getStringDate();
        add_type_startdate.setText(nowdate);
        add_type_enddate.setText(nowdate);

        sfzg_map.put("是", "1");
        sfzg_map.put("否", "0");
        cffl_map.put("一般程序", "1");
        cffl_map.put("简易程序", "0");
        //默认单选选择
        add_type_zgyes.setChecked(true);
        add_type_ybcx.setChecked(true);

        setRadioButton();

        imagePaths = new ArrayList<String>();
        list = new ArrayList<>();

        //设置gridview一行多少个
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        job_add_gridview.setNumColumns(cols);
        AddImage();

        imagePaths.add(myCode);
        gridAdapter = new CommonlyGridViewAdapter(this, imagePaths);
        job_add_gridview.setAdapter(gridAdapter);
    }

    private void AddImage() {
//        //添加多张图片
//        job_add_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (ContextCompat.checkSelfPermission(AddType.this, SDUtils.CameraPermission) != PackageManager.PERMISSION_GRANTED) {
//                    MyPermission();
//                } else {
//                    String imgs = (String) parent.getItemAtPosition(position);
//                    if (myCode.equals(imgs)) {
//                        if (job_add_gridview.getCount() >= 6) {
//                            ShowToast.showShort(AddType.this, "最多添加6张图片");
//                        } else {
//                            createFile();
//                            picture = new File(savePath, System.currentTimeMillis() + ".jpg");
//                            cameraFileUri = Uri.fromFile(picture);
//                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
//                            startActivityForResult(intent, REQUEST_CAMERA_CODE);
//                        }
//                    } else {
//                        PhotoPreviewIntent intent = new PhotoPreviewIntent(AddType.this);
//                        imagePaths.remove(imagePaths.size() - 1);
//                        intent.setCurrentItem(position);
//                        intent.setPhotoPaths(imagePaths);
//                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
//                    }
//                }
//
//            }
//        });
        //添加多张图片
        job_add_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgs = (String) parent.getItemAtPosition(position);
                clickPosition = position;
                AndPermission.with(AddType.this)
                        .requestCode(200)
                        .permission(
                                // 申请多个权限组方式：
                                PermissionUtil.CameraPermission,
                                PermissionUtil.WriteFilePermission,
                                PermissionUtil.LocationPermission
                        ).send();

            }
        });
    }

    private void ClickAdd(int position) {
        if (SDUtils.hasSdcard()) {
            if (myCode.equals(imgs)) {
//                    PhotoPickerIntent intent = new PhotoPickerIntent(AddCommonly.this);
//                    imagePaths.remove(imagePaths.size() - 1);
//                    intent.setSelectModel(SelectModel.MULTI);
//                    intent.setShowCarema(true); // 是否显示拍照
//                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
//                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
//                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                if (job_add_gridview.getCount() >= 7) {
                    ShowToast.showShort(AddType.this, "最多添加6张图片");
                } else {
                    OpenCamera();
                }
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(AddType.this);
                imagePaths.remove(imagePaths.size() - 1);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        } else {
            ShowToast.showShort(AddType.this, "没有SD卡");
        }
    }

    /**
     * 打开相机
     */
    private void OpenCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            createFile();
            picture = new File(savePath, System.currentTimeMillis() + ".jpg");
            cameraFileUri = FileProvider.getUriForFile(AddType.this, "com.example.administrator.dimine_yizhang.fileprovider", picture);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
        } else {
            ShowToast.showShort(AddType.this, "无相机应用");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                ClickAdd(clickPosition);
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(AddType.this, "拍照失败，需要开启权限");
            }
        }
    };

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


    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //省信息+城市信息+城区信息+街道信息
            local = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet();
        }
    }


    private void setRadioButton() {
        add_type_radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == add_type_zgyes.getId()) {
                    sfzg = "是";
                } else {
                    sfzg = "否";
                }
            }
        });

        add_type_radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == add_type_ybcx.getId()) {
                    cffl = "一般程序";
                } else {
                    cffl = "简易程序";
                }
            }
        });

    }


    private void mOkhttp() {
        PostFormBuilder builder = null;
        builder = OkHttpUtils
                .post()
                .url(PortIpAddress.AddXccheckrecord())
                .addParams("access_token", PortIpAddress.getToken(this))
                .addParams("bjcdw", qyid)
                .addParams("address", add_type_address.getText().toString())
                .addParams("frdb", add_type_frdb.getText().toString())
                .addParams("zw", add_type_zw.getText().toString())
                .addParams("jccs", add_type_jccs.getText().toString())
                .addParams("starttime", add_type_startdate.getText().toString())
                .addParams("endtime", add_type_enddate.getText().toString())
                .addParams("zbjg", add_type_zfxzjg.getText().toString())
                .addParams("zbry", add_type_zbry.getText().toString())
                .addParams("zbjz", add_type_zbryzjh.getText().toString())
                .addParams("xbry", add_type_xbry.getText().toString())
                .addParams("xbzj", add_type_xbryzjh.getText().toString())
                .addParams("remark", add_type_jcms.getText().toString())
                .addParams("sfxyzg", sfzg_map.get(sfzg))
                .addParams("xzcffl", cffl_map.get(cffl))
                .addParams("jcdwmc", add_type_jcdw.getText().toString());

        //添加图片绝对路径到builder，并约定key“files”作为后台接受多张图片的key
        for (int i = 0; i < imagePaths.size() - 1; i++) {
            File file = new File(imagePaths.get(i));
            builder.addFile("files", file.getName(), file);
        }

        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast.showShort(AddType.this, "添加失败,请检查网络情况");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                Message message = handler.obtainMessage();
                                message.what = 1;
                                handler.sendMessage(message);
                            } else {
                                ShowToast.showShort(AddType.this, "添加失败，请重新添加");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    protected void setOnClick() {
        add_type_back.setOnClickListener(this);
        add_type_startdate.setOnClickListener(this);
        add_type_enddate.setOnClickListener(this);
        add_type_submit.setOnClickListener(this);
        add_type_bjcdw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_type_back:
                finish();
                break;
            case R.id.add_type_bjcdw:
                Intent intent = new Intent(AddType.this, SearchQy.class);
                startActivityForResult(intent, 10);
                break;

            case R.id.add_type_startdate:
                if (first_start) {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                add_type_startdate.setText(select_date);
                                yearStartDate = year;
                                monthStartDate = monthOfYear;
                                dayStartDate = dayOfMonth;
                                first_start = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog_date.show();
                } else {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                add_type_startdate.setText(select_date);
                                yearStartDate = year;
                                monthStartDate = monthOfYear;
                                dayStartDate = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, yearStartDate, monthStartDate, dayStartDate);
                    dialog_date.show();
                }
                break;
            case R.id.add_type_enddate:
                if (first_end) {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                add_type_enddate.setText(select_date);
                                yearEndDate = year;
                                monthEndDate = monthOfYear;
                                dayEndDate = dayOfMonth;
                                first_end = false;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialog_date.show();
                } else {
                    DatePickerDialog dialog_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                String select_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = formatter.parse(select_time);
                                String select_date = formatter.format(date);
                                // TODO Auto-generated method stub
                                add_type_enddate.setText(select_date);
                                yearEndDate = year;
                                monthEndDate = monthOfYear;
                                dayEndDate = dayOfMonth;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, yearEndDate, monthEndDate, dayEndDate);
                    dialog_date.show();
                }
                break;

            case R.id.add_type_submit:
                long begintime = DateUtils.getStringToDate(add_type_startdate.getText().toString());
                long endtime = DateUtils.getStringToDate(add_type_enddate.getText().toString());
                if (add_type_address.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请选择被检查单位");
                } else if (add_type_jccs.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写检查场所");
                } else if (add_type_zfxzjg.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写执法行政机关");
                } else if (add_type_zbry.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写主办人员");
                } else if (add_type_zbryzjh.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写主办人员证件号");
                } else if (add_type_jcdw.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写检查单位名称");
                } else if (add_type_jcms.getText().toString().trim().equals("")) {
                    ShowToast.showShort(AddType.this, "请填写检查描述");
                } else if (endtime - begintime < 0) {
                    ShowToast.showShort(this, "日期选择有误");
                } else {
                    mOkhttp();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            if (resultCode == RESULT_OK) {
//
//            }
//        }
        Log.e(TAG, resultCode + "");

        if (resultCode == SearchQy.RESULT_SEARCHQY) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                qyid = bundle.getString("qyid");
                add_type_bjcdw.setText(bundle.getString("qyname"));
            }
        } else if (resultCode == RESULT_OK) {
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
                    SDUtils.refreshAlbum(AddType.this, picture);
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


    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    setResult(Activity.RESULT_OK);
                    ShowToast.showShort(AddType.this, "提交成功");
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
