package cn.leeq.util.memodemo.ui;

import android.os.Bundle;
import android.util.Log;

import org.xutils.db.converter.ColumnConverterFactory;

import java.io.IOException;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.Yu;
import cn.leeq.util.memodemo.bean.Status;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LeeQ
 * Date : 2016-8-8
 * Name : MemoDemo
 * Use : Retrofit请求demo
 */
public class RetrofitDemo extends BaseActivity {

    public static final String API_URL = "https://api.github.com";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws IOException {
        Yu yu = retrofit.create(Yu.class);

        Call<List<Status>> listCall = yu.withYu("NicolasKun", "memodemo");

        listCall.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                List<Status> body = response.body();
                for (Status status : body) {
                    Log.e("retrofit", "结果码 " + response.code() + "\n结果 " + status.getContributions());
                }
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }
}
