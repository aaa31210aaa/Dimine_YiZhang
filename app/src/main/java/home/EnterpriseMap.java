package home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.administrator.dimine_yizhang.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import utils.AMapUtil;
import utils.BaseActivity;
import utils.SDUtils;
import utils.ShowToast;

public class EnterpriseMap extends BaseActivity implements
        LocationSource
        , AMapLocationListener
        , OnMarkerClickListener
        , OnMapClickListener
        , GeocodeSearch.OnGeocodeSearchListener
        , InfoWindowAdapter {
    private MapView enterprise_map;
    private AMap aMap;
    //地图UI设置
    private UiSettings mUiSettings;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 10;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //保存当前marker
    private Marker currMarker;

    //企业名字
    private String qyname;
    private GeocodeSearch geocoderSearch;
    private double lng = 0;
    private double lat = 0;
    private LatLng qylatlng;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_map);
        initView();
        enterprise_map.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        enterprise_map = (MapView) findViewById(R.id.enterprise_map);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MapPermission();
        } else {
            initData();
            setOnClick();
        }

//        //配置权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            //申请WRITE_EXTERNAL_STORAGE权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
//        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    //询问权限
    private void MapPermission() {
        AndPermission.with(this)
                .requestCode(SDUtils.LOCATION_REQUESTCODE)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
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
            if (requestCode == SDUtils.LOCATION_REQUESTCODE) {
                initData();
                setOnClick();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == SDUtils.LOCATION_REQUESTCODE) {
                ShowToast.showShort(EnterpriseMap.this, "定位失败,需开启定位权限");
//                MapPermission();
//                finish();
            }
        }
    };


    @Override
    protected void initData() {
        Intent intent = getIntent();
        qyname = intent.getStringExtra("qyname");
//        //逆地理编码获得经纬度
//        getLatlon(qyname, "郴州市");
        if (!intent.getStringExtra("lng").equals("") && !intent.getStringExtra("lat").equals("")) {
            lng = Double.parseDouble((intent.getStringExtra("lng")));
            lat = Double.parseDouble(intent.getStringExtra("lat"));
            qylatlng = new LatLng(lat, lng);
        }
        initAmap();
    }

    //初始化地图
    private void initAmap() {
        aMap = enterprise_map.getMap();
        mUiSettings = aMap.getUiSettings();
        //开启比例尺
        mUiSettings.setScaleControlsEnabled(true);
        //开启指南针
        mUiSettings.setCompassEnabled(true);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        //设置显示定位按钮 并且可以点击
//        UiSettings settings = aMap.getUiSettings();
//        // 是否显示定位按钮
//        settings.setMyLocationButtonEnabled(true);
        //开启室内图
        aMap.showIndoorMap(true);
    }


    @Override
    protected void setOnClick() {
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);
        ;// 添加显示infowindow监听事件
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name, String city) {
        GeocodeQuery query = new GeocodeQuery(name, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }


    //标记点击监听
    @Override
    public boolean onMarkerClick(Marker marker) {
        currMarker = marker;
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currMarker.isInfoWindowShown()) {
            currMarker.hideInfoWindow();//这个是隐藏infowindow窗口的方法
        }
    }


    /**
     * 绘制marker
     */
    private void AddMarkerImage(LatLng latLng, String str) {
        aMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("企业名称：")
                .snippet(str)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dingwei)))
        );
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //获取一次定位结果
            mLocationOption.setOnceLocation(true);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
        if (isFirstLoc) {
            AddMarkerImage(qylatlng, qyname);
            isFirstLoc = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        enterprise_map.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        enterprise_map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        enterprise_map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        enterprise_map.onSaveInstanceState(outState);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }


    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                AddMarkerImage(AMapUtil.convertToLatLng(address.getLatLonPoint()), qyname);

                Log.e(TAG, rCode + "=============");
            } else {
                ShowToast.showShort(EnterpriseMap.this, "没有查询到结果");
                Log.e(TAG, rCode + "=============");
            }
        } else {
            Log.e(TAG, rCode + "=============");
            ShowToast.showShort(EnterpriseMap.this, R.string.network_error);
        }
    }


    //自定义infowindow布局
    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.daohang,
                null);
        TextView title = (TextView) view.findViewById(R.id.daohang_title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.daohang_snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
