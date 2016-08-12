package cn.leeq.util.memodemo.ui;

import android.os.Bundle;

import java.io.IOException;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.Yu;
import cn.leeq.util.memodemo.bean.Status;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by LeeQ
 * Date : 2016-8-8
 * Name : MemoDemo
 * Use : Retrofit请求demo
 */
public class RetrofitDemo extends BaseActivity {

    public static final String API_URL = "";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .build();

        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws IOException {
        Yu yu = retrofit.create(Yu.class);
        Response<Status> create_name = yu.withYu("create_name").execute();
        String awb = create_name.body().getAwb();
    }
}
