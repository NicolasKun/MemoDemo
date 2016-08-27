package cn.leeq.util.memodemo.ui;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.bean.NearbyLocation;
import cn.leeq.util.memodemo.config.Constants;

/**
 * 显示Markers
 */
public class LocationInfoDemo extends AppCompatActivity implements AMap.OnInfoWindowClickListener, LocationSource, AMapLocationListener, AMap.OnMapLoadedListener {

    private MapView                       mapView;
    private AMap                          aMap;
    private List<NearbyLocation.ListBean> data = new ArrayList<>();
    private OnLocationChangedListener     mListener;
    private AMapLocationClient            mlocationClient;
    private AMapLocationClientOption      mLocationOption;
    private LatLng                        mLatlng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info_demo);
        mapView = (MapView) findViewById(R.id.li_map_view);
        mapView.onCreate(savedInstanceState);

        loadData();

        init();
    }

    /**
     * 获取附近定位
     */
    private void loadData() {
        NearbyLocation location = new Gson().fromJson(Constants.DATA_FOR_LOCATION, NearbyLocation.class);
        List<NearbyLocation.ListBean> list = location.getList();
        data.addAll(list);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));
        locationStyle.strokeColor(Color.BLACK);
        locationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
        locationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(locationStyle);
        aMap.setLocationSource(this);  //设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true); // 设置为true表示显示定位层并可触发定位  默认false
        aMap.setOnInfoWindowClickListener(this); //点击infoWindow
        aMap.setOnMapLoadedListener(this);
        addMarkersToMap();
    }

    /**
     * 添加marker
     */
    private void addMarkersToMap() {
        Log.e("test", "位置集合 " + data.size());
        if (data != null) {
            for (final NearbyLocation.ListBean bean : data) {
                double posy = bean.getPosy();
                double posx = bean.getPosx();
                LatLng latLng = new LatLng(posy, posx);
                aMap.addMarker(new MarkerOptions()
                        .anchor(0.5f, 0.5f)
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.image_emoticon25))
                        .title(bean.getMemname())
                        .draggable(true));
            }
        }
    }

    //必须
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    //必须
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    //必须
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    //必须
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 点击infoWindow
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(LocationInfoDemo.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //高精度
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && mListener != null) {
            //Log.e("test", "定位代码 " + aMapLocation.getErrorCode());
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);
                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                mLatlng = new LatLng(latitude, longitude);

                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.fromLatLngZoom(mLatlng,16)));
                mlocationClient.stopLocation();
                //Log.e("test", "---Location---");
            } else {
                String errText = "定位失败," + aMapLocation.getErrorInfo();
                Log.e("test", errText + "\n代码 " + aMapLocation.getErrorCode());
            }
        }
    }


    @Override
    public void onMapLoaded() {
        Log.e("test", "--onMapLoaded--");
    }
}


