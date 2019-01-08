package tab;


import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.dimine_yizhang.CircularImage;
import com.example.administrator.dimine_yizhang.Login;
import com.example.administrator.dimine_yizhang.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.bingoogolapple.badgeview.BGABadgeRelativeLayout;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;
import home.MyNotification;
import mine.ModifyPassword;
import mine.MyFavorActivity;
import mine.MyPartActivity;
import mine.MyTopicActivity;
import okhttp3.Call;
import utils.BaseFragment;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.SDUtils.CAMERA_REQUESTCODE;
import static utils.SDUtils.CameraPermission;
import static utils.SDUtils.STORAGE_REQUESTCODE;
import static utils.SDUtils.WriteFilePermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mine extends BaseFragment {
    private View view;
    private CircularImage mine_head_photo;
    //    private RelativeLayout mine_pyq;
    private TextView mine_name;
    private String username;
    //身份待审核
    private TextView mine_identity;
    //下级人数
    private TextView mine_xj_num;
    //我的职责
    private BGABadgeRelativeLayout mine_duty;
    //意见反馈
    private BGABadgeRelativeLayout mine_opinion;
    //我发表的话题
    private BGABadgeRelativeLayout mine_publish_topic;
    //我参与的话题
    private BGABadgeRelativeLayout mine_partake_topic;
    private BGABadgeRelativeLayout mine_favor_topic;
    //消息通知
    private BGABadgeRelativeLayout mine_notification;
    //修改密码
    private BGABadgeRelativeLayout mine_modify_password;
    //注销
    private RelativeLayout mine_cancellation;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private Intent intent;
    private String url;
    private String notiUrl;
    private String readUrl;
    private String userid;
    private String user_token;
    private String headurl;
    //身份
    private TextView mine_sf;

    private String MyUrl = "http://192.168.5.228:9788/LEMS/";

    private Dialog mCameraDialog;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 相机
    private static final int PHOTO_REQUEST_GALLERY = 2;// 相册
    private static final int PHOTO_REQUEST_CUT = 3;// 裁剪


    private File cameraFile;
    private Uri cameraUri;
    private Uri galleryUri;
    private File cropFile;
    private Uri cropUri;
    private String savePath;


    public Mine() {
        // Required empty public constructor
    }


    @Override
    public View makeView() {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_mine, null);
            initView();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void loadData() {
        initData();
        setOnClick();
    }

    @Override
    protected void initView() {
        mine_identity = (TextView) view.findViewById(R.id.mine_identity);
        mine_sf = (TextView) view.findViewById(R.id.mine_sf);
        mine_xj_num = (TextView) view.findViewById(R.id.mine_xj_num);
        mine_duty = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_duty);
        mine_opinion = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_opinion);
        mine_publish_topic = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_publish_topic);
        mine_partake_topic = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_partake_topic);
        mine_favor_topic = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_favor_topic);
        mine_notification = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_notification);
        mine_modify_password = (BGABadgeRelativeLayout) view.findViewById(R.id.mine_modify_password);
        mine_cancellation = (RelativeLayout) view.findViewById(R.id.mine_cancellation);
//        mine_pyq = (RelativeLayout) view.findViewById(R.id.mine_pyq);

        mine_name = (TextView) view.findViewById(R.id.mine_name);
        mine_head_photo = (CircularImage) view.findViewById(R.id.mine_head_photo);
        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        url = PortIpAddress.SaveHead();
        notiUrl = PortIpAddress.MessageList();
        readUrl = PortIpAddress.AllReadInfo();
        userid = SharedPrefsUtil.getValue(getActivity(), "userInfo", "userid", null);
        user_token = SharedPrefsUtil.getValue(getActivity(), "userInfo", "user_token", null);

    }


    @Override
    protected void initData() {
        String sf = SharedPrefsUtil.getValue(getActivity(), "userInfo", "sf", null);
        if (sf.equals("null")) {
            mine_sf.setText("");
        } else {
            mine_sf.setText(sf);
        }

        if (SharedPrefsUtil.getValue(getActivity(), "userInfo", "headurl", null).equals("")) {
            mine_head_photo.setImageResource(R.drawable.default_headimg);
        } else {
            String headpath = SharedPrefsUtil.getValue(getActivity(), "userInfo", "headurl", null);
            headpath = PortIpAddress.host.replace("mobile/", "") + headpath.replace("\\", "/");
            Glide
                    .with(getActivity())
                    .load(headpath)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(mine_head_photo);
        }
        mine_name.setText(SharedPrefsUtil.getValue(getActivity(), "userInfo", "username", null));
        setNotificationNum();
    }

    private void initBadgeView(String num) {
        //设置红点数字
        mine_notification.showTextBadge(num);
        //设置可拖拽
        mine_notification.getBadgeViewHelper().setDragable(true);
        mine_notification.getBadgeViewHelper().setBadgeHorizontalMarginDp(50);
        mine_notification.getBadgeViewHelper().isResumeTravel();
//        mine_notification.getBadgeViewHelper().setBadgeGravity(BGAG);
        //监听拖拽消失后的操作
        mine_notification.setDragDismissDelegage(new BGADragDismissDelegate() {
            @Override
            public void onDismiss(BGABadgeable badgeable) {
                AllRead();
                ShowToast.showShort(getActivity(), "全部已读");
            }
        });
    }


    /**
     * 设置选择头像的dialog
     */
    private void setUserPhoto() {
        mCameraDialog = new Dialog(getActivity(), R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.mine_camera_control, null);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        mCameraDialog.setCanceledOnTouchOutside(true);//点击dialog外部消失
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels - 100); // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }


    /**
     * 相机
     */
    private void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断有无SD卡
        if (SDUtils.hasSdcard()) {
            createFile();

//            Bitmap bitmap = BitmapFactory.decodeFile(savePath+System.currentTimeMillis() + ".jpg");
//            //获得旋转角度
//            int degree = WaterImage.getBitmapDegree(savePath+System.currentTimeMillis() + ".jpg");
//            //矫正照片被旋转
//            bitmap = WaterImage.rotateBitmapByDegree(bitmap, degree);

            cameraFile = new File(savePath, System.currentTimeMillis() + ".jpg");
            cameraUri = Uri.fromFile(cameraFile);
            cropFile = new File(savePath, System.currentTimeMillis() + "_cropimage" + ".jpg");
            cropUri = Uri.fromFile(cropFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }


    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {
        savePath = SDUtils.sdPath + "/DCIM/Crop/";
        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    /**
     * 裁剪
     *
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 768);
        intent.putExtra("outputY", 1280);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 相册选择
     */
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        createFile();
        cropFile = new File(savePath, System.currentTimeMillis() + "_cropimage" + ".jpg");
        cropUri = Uri.fromFile(cropFile);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        SDUtils.refreshAlbum(getActivity(), cropFile);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }


    @Override
    protected void setOnClick() {
//        mine_pyq.setOnClickListener(this);
        mine_cancellation.setOnClickListener(this);
        mine_modify_password.setOnClickListener(this);
        mine_head_photo.setOnClickListener(this);
        mine_publish_topic.setOnClickListener(this);
        mine_partake_topic.setOnClickListener(this);
        mine_favor_topic.setOnClickListener(this);
        mine_notification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_modify_password:
                intent = new Intent(getActivity(), ModifyPassword.class);
                startActivity(intent);
                break;
            //我发布的话题
            case R.id.mine_publish_topic:
                intent = new Intent(getActivity(), MyTopicActivity.class);
                startActivity(intent);
                break;
            //我参与的话题
            case R.id.mine_partake_topic:
                intent = new Intent(getActivity(), MyPartActivity.class);
                startActivity(intent);
                break;
            //我关注的话题
            case R.id.mine_favor_topic:
                intent = new Intent(getActivity(), MyFavorActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_notification:
                intent = new Intent(getActivity(), MyNotification.class);
                startActivity(intent);
                break;

//            case R.id.mine_pyq:
//                intent = new Intent(getActivity(), Test.class);
//                startActivity(intent);
//                break;
            case R.id.mine_cancellation:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.mine_cancellation_dialog_title);
                builder.setMessage(R.string.mine_cancellation_dialog_content);
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(getActivity(), Login.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                        editor.remove("USER_NAME");
                        editor.commit();
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                break;

            case R.id.mine_head_photo:
                setUserPhoto();
                break;
            //相机
            case R.id.btn_open_camera:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    CameraPermission();
                } else {
                    camera();
                }

                break;
            //相册
            case R.id.btn_choose_img:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                    GalleryPermission();
                } else {
                    gallery();
                }
                break;
            case R.id.btn_cancel:
                mCameraDialog.dismiss();
                break;

        }
    }

    //相机询问权限
    private void CameraPermission() {
        AndPermission.with(getActivity())
                .requestCode(CAMERA_REQUESTCODE)
                .permission(CameraPermission)
                .send();
    }

    //相册询问权限
    private void GalleryPermission() {
        AndPermission.with(getActivity())
                .requestCode(STORAGE_REQUESTCODE)
                .permission(WriteFilePermission)
                .send();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == CAMERA_REQUESTCODE) {
                Log.e(TAG, "相机授权成功");
                camera();
            } else if (requestCode == STORAGE_REQUESTCODE) {
                gallery();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == CAMERA_REQUESTCODE) {
                ShowToast.showShort(getActivity(), "调用相机失败,需开启相机权限");
            } else if (requestCode == STORAGE_REQUESTCODE) {
                ShowToast.showShort(getActivity(), "调用相册失败,需开启文件读写权限");
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                if (data != null) {
                    galleryUri = data.getData();
                    crop(galleryUri);
                }

            } else if (requestCode == PHOTO_REQUEST_CAMERA) {
                if (SDUtils.hasSdcard()) {
                    crop(cameraUri);
                } else {
                    ShowToast.showShort(getActivity(), "没有找到SD卡");
                }

            } else if (requestCode == PHOTO_REQUEST_CUT) {
                Glide
                        .with(getActivity())
                        .load(cropUri)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(mine_head_photo);
                SDUtils.refreshAlbum(getActivity(), cropFile);


                File file = new File(cropUri.getPath());
                mOkhttp("file", file.getName(), file);

            }
        }

    }

    private void mOkhttp(String key, String fileName, File file) {
        OkHttpUtils
                .post()
                .url(url)
                .addParams("access_token", user_token)
                .addFile(key, fileName, file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response + "---");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                //拍照后原图删除留裁剪的照片
                                if (cropFile != null || cropFile.equals("")) {
                                    cropFile.delete();
                                }
                            } else {
                                ShowToast.showShort(getActivity(), "上传失败,请重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        setNotificationNum();
    }

    //设置未读通知条数
    private void setNotificationNum() {
        OkHttpUtils
                .get()
                .url(notiUrl)
                .addParams("access_token", user_token)
                .addParams("userid", userid)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String count = jsonObject.getString("count");
                            if (!count.equals("0")) {
                                initBadgeView(count);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //设置全部已阅
    private void AllRead() {
        OkHttpUtils
                .get()
                .url(readUrl)
                .addParams("access_token", user_token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);
                    }
                });
    }

}
