package cn.leeq.util.memodemo.widget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.adapter.PcdAdapter;
import cn.leeq.util.memodemo.bean.AddrBean;

public class PCDPopUpWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private final String mTable_province = "province";
    private final String mTable_city     = "city";
    private final String mTable_district = "district";
    private final int    mLoad_province  = 100;
    private final int    mLoad_city      = 101;
    private final int    mLoad_district  = 102;
    private Context  mContext;
    private View     mView;
    private ListView mLv_province; // 省列表
    private ListView mLv_city;      // 城市列表
    private ListView mLv_district;  // 地区列表
    private int            proCityPosition     = -1;   // 上一个城市的索引
    private int            proProvincePosition = -1;   // 上一个省的索引
    private int            proDistrictPosition = -1;   // 上一个地区的索引
    private List<AddrBean> mProvinceDatas      = new ArrayList<>(); // 省份集合
    private List<AddrBean> mCityDatas          = new ArrayList<>(); // 城市集合
    private List<AddrBean> mDistrictDatas      = new ArrayList<>(); // 地区集合
    private PcdAdapter mCityAdapter;
    private       PcdAdapter            mProvinceAdapter;
    private       PcdAdapter            mDistrictAdapter;
    private final OnCheckChangeListener mOnCheckChangeListener;

    private SQLiteDatabase mDb;

    private File file;
    private String mFileName;
    // 因为要先初始化数据库
    // 所以数据库文件在创建PopUpWindow时传入
    public PCDPopUpWindow(Context context, String fileName, OnCheckChangeListener listener) {
        this.mFileName = fileName;
        this.mContext = context;
        this.mOnCheckChangeListener = listener;
        // PopUpWindow的布局由三个ListView组成
        mView = View.inflate(mContext, R.layout.pop_pcd_listview, null);
        setContentView(mView);
        // 设置宽高
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        // 使其可点击
        // 点击外部可以消失
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DBDBDB")));

        initView();

        initEvent();

        initDb();
        // 先初始化省的列表
        loadCityData(mTable_province, null,0);
    }

    private void initDb() {
        file = new File(mContext.getFilesDir(), mFileName);
        if (!file.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream is = null;
                    FileOutputStream fos = null;
                    try {
                        is = mContext.getAssets().open(mFileName);
                        fos = new FileOutputStream(file);

                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        is.close();
                        fos.close();
                    } catch (Exception e) {
                        Log.e("test", "读取数据库 " + e.getMessage());
                    }
                }
            }).start();
        }

        // 打开已有的数据库文件
        if (mDb == null) {
            mDb = SQLiteDatabase.openDatabase(file.getAbsolutePath(),
                    null, SQLiteDatabase.OPEN_READWRITE);
        }
    }


    public void initEvent() {
        mLv_province.setOnItemClickListener(this);
        mLv_city.setOnItemClickListener(this);
        mLv_district.setOnItemClickListener(this);
    }

    private void initView() {
        mLv_province = (ListView) mView.findViewById(R.id.lv_province);
        mLv_city = (ListView) mView.findViewById(R.id.lv_city);
        mLv_district = (ListView) mView.findViewById(R.id.lv_district);
    }

    /**
     * 选择地区的事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_province:
                // 点击省份列表时要将城市和地区的数据清空
                // 并将城市的前一个索引设为-1,防止选择城市只有一个的省时发生越界
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.setCityText("",-1);
                    mOnCheckChangeListener.setDistrictText("",-1);
                }
                mDistrictDatas.clear();
                mCityDatas.clear();
                proCityPosition = -1;

                // 根据是否有上一个选择的省份
                // 有的话就将上的状态置为false,以改变背景
                if (proProvincePosition != -1) {
                    AddrBean proAreaBean = (AddrBean) parent.getItemAtPosition(proProvincePosition);
                    proAreaBean.isChoose = false;
                }
                // 将当前的省份赋给上一个省份
                // 并把当前的状态设为已选中
                proProvincePosition = position;
                AddrBean areaBean = (AddrBean) parent.getItemAtPosition(position);
                areaBean.isChoose = true;

                // 监听选择的省份,把数据用接口传给MainActivity
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.setProvinceText(areaBean.getAddrName(),areaBean.getProvinceId());
                }
                // 更新省份的ListView
                mProvinceAdapter.notifyDataSetChanged();
                // 根据当前省数据的code加载城市列表
                // 从数据库中查询城市数据
                loadCityData(mTable_city, areaBean.getProvinceId()+"",1);
                break;

            case R.id.lv_city:
                // 选择城市列表需要将之前选择的地区清除
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.setDistrictText("",-1);
                }
                // 更新城市item的背景
                // 同省份
                if (proCityPosition != -1) {
                    AddrBean cityAreaBean = (AddrBean) parent.getItemAtPosition(proCityPosition);
                    cityAreaBean.isChoose = false;
                }

                mDistrictDatas.clear();

                proCityPosition = position;
                AddrBean cityAreaBean = (AddrBean) parent.getItemAtPosition(position);
                cityAreaBean.isChoose = true;

                String city_id = cityAreaBean.getCityId() + "";
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.setCityText(cityAreaBean.getAddrName(),cityAreaBean.getCityId());
                }
                mCityAdapter.notifyDataSetChanged();
                
                // 根据市级数据的code加载区域列表
                loadCityData(mTable_district, city_id,2);
                break;
            // 选择地区的item,同省份
            case R.id.lv_district:
                if (proDistrictPosition != -1) {
                    AddrBean districtAreaBean = (AddrBean) parent.getItemAtPosition(proDistrictPosition);
                    districtAreaBean.isChoose = false;
                }
                proDistrictPosition = position;

                AddrBean districtAreaBean = (AddrBean) parent.getItemAtPosition(position);
                districtAreaBean.isChoose = true;
                /*Toast.makeText(mContext, "地区 " + districtAreaBean.getAddrName() + "\np_id " + districtAreaBean.getProvinceId() +
                        "\nc_id " + districtAreaBean.getCityId() + "\na_id " + districtAreaBean.getAreaId(), Toast.LENGTH_SHORT).show();*/
                /*Log.e("test", "地区 " + districtAreaBean.getAddrName() + "\np_id " + districtAreaBean.getProvinceId() +
                        "\nc_id " + districtAreaBean.getCityId() + "\na_id " + districtAreaBean.getAreaId());*/
                if (mOnCheckChangeListener != null) {
                    mOnCheckChangeListener.setDistrictText(districtAreaBean.getAddrName(),districtAreaBean.getAreaId());
                }
                mDistrictAdapter.notifyDataSetChanged();
                dismiss();
                break;
        }
    }

    // 开启线程来加载数据
    public void loadCityData(final String table_name, final String pcode,final int index) {
        new Thread() {
            @Override
            public void run() {
                queryPcdDB(table_name, pcode,index);
            }
        }.start();
    }

    /**
     * 用来点击选择显示文本的回调
     */
    public interface OnCheckChangeListener {

        void setCityText(String cityText, int cityId);

        void setProvinceText(String provinceText, int provinceId);

        void setDistrictText(String areaText, int areaId);

    }


    /**
     * 查询数据库的操作
     */
    public void queryPcdDB(String table, String pcode,int index)  {
        List<AddrBean> datas_p = new ArrayList<>();
        List<AddrBean> datas_c = new ArrayList<>();
        List<AddrBean> datas_a = new ArrayList<>();

        String sql = " select * from " + table;

        if (pcode != null) {
            if (index == 1) {
                sql = " select * from " + table + " where pid=" + pcode;
            } else if (index == 2) {
                sql = " select * from " + table + " where cid=" + pcode;
            }
        }
        Cursor cursor = mDb.rawQuery(sql, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                if (index == 0) {
                    AddrBean bean = new AddrBean();
                    bean.setAddrName(name);
                    bean.setProvinceId(id);
                    datas_p.add(bean);
                } else if (index == 1) {
                    AddrBean bean = new AddrBean();
                    bean.setAddrName(name);
                    bean.setCityId(id);
                    datas_c.add(bean);
                } else if (index == 2) {
                    int p_id = cursor.getInt(2);
                    int c_id = cursor.getInt(3);

                    AddrBean bean = new AddrBean();
                    bean.setAddrName(name);
                    bean.setAreaId(id);
                    bean.setCityId(c_id);
                    bean.setProvinceId(p_id);
                    datas_a.add(bean);
                }

            }
        }
        cursor.close();

        // 查询在做是在子线程中的,查询结束后发送message设置adapter
        Message msg = Message.obtain();

        switch (table) {
            case mTable_province:
                msg.what = mLoad_province;
                msg.obj = datas_p;
                break;

            case mTable_city:
                msg.what = mLoad_city;
                msg.obj = datas_c;
                break;

            case mTable_district:
                msg.what = mLoad_district;
                msg.obj = datas_a;
                break;
        }

        mHandler.sendMessage(msg);

    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        mDb.close();
    }

    /**
     * 用来对listview设置adapter的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<AddrBean> list = (List<AddrBean>) msg.obj;
            switch (msg.what) {
                case mLoad_province:
                    // 加载省列表
                    mProvinceDatas.addAll(list);
                    mProvinceAdapter = new PcdAdapter(mContext, mProvinceDatas);
                    mLv_province.setAdapter(mProvinceAdapter);
                    break;
                case mLoad_city:
                    // 加载城市列表
                    mCityDatas.addAll(list);
                    mCityAdapter = new PcdAdapter(mContext, mCityDatas);
                    mLv_city.setAdapter(mCityAdapter);
                    break;
                case mLoad_district:
                    // 加载地区列表
                    mDistrictDatas.addAll(list);
                    mDistrictAdapter = new PcdAdapter(mContext, mDistrictDatas);
                    mLv_district.setAdapter(mDistrictAdapter);
                    break;
            }
        }
    };


}
