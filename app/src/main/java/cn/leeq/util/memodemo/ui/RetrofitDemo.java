package cn.leeq.util.memodemo.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import cn.leeq.util.memodemo.R;
import cn.leeq.util.memodemo.Yu;
import cn.leeq.util.memodemo.bean.Status;
import retrofit2.Call;
import retrofit2.Callback;
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
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_demo);
        tvResult = (TextView) findViewById(R.id.rd_tv_result);
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private String test(String all) {
        if (all.equals("") && all.length() <= 0) {
            throw new NullPointerException("Do not found everything in \'all\'");
        }
        if (all.split(" ").length <= 0) {
            throw new IllegalArgumentException("Do not found \' \' in \'all\' ,please check");
        }
        String[] split = all.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            String a = split[i];
            if (a.length() == 1) {
                sb.append(a + " ");
            } else {
                String aa = a.substring(0, 1) + "ay";  //截取头字母
                String bb = a.substring(1, a.length()); //剩下的字符串
                String current = bb + aa;
                sb.append(current + " ");
            }
        }
        System.out.println("结果\n" + sb.toString());
        return sb.toString();
    }

    private void loadData() throws IOException {
        Yu yu = retrofit.create(Yu.class);

        Call<List<Status>> listCall = yu.withYu("NicolasKun", "memodemo");

        listCall.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                List<Status> body = response.body();
                for (Status status : body) {
                    String s = "结果    " + status.getContributions() +
                            "\n登陆者 " + status.getLogin() + "\n登陆ID " + status.getId();
                    tvResult.setText(s);
                    Log.e("retrofit", "结果码 " + response.code() + "\n" + s);
                }
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    public void getResult(View view) {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
