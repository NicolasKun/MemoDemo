package cn.leeq.util.memodemo.ui;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.bean.NearbyLocation;
import cn.leeq.util.memodemo.config.Constants;

/**
 * 显示Markers
 */
public class LocationInfoDemo extends AppCompatActivity implements AMap.OnInfoWindowClickListener {

    private MapView mapView;
    private AMap aMap;
    private List<NearbyLocation.ListBean> data = new ArrayList<>();
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
        aMap.setOnInfoWindowClickListener(this); //点击infoWindow
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

}


