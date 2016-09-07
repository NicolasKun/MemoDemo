package cn.leeq.util.memodemo;

import cn.leeq.util.memodemo.bean.CourierInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LeeQ
 * Date : 2016-09-06
 * Name : MemoDemo
 * Use :
 */
public interface CourierLoginBack {

    @FormUrlEncoded
    @POST("kd/mlogin")
    Call<CourierInfo> getInfo(
            @Field("user.loginid") String loginid,
            @Field("user.password") String password
    );
}
