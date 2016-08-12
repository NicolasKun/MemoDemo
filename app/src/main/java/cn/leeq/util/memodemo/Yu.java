package cn.leeq.util.memodemo;


import cn.leeq.util.memodemo.bean.Status;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Yu {
    @GET("/kdb/images/{creator}")
    Call<Status> withYu(@Path("creator") String creator);
}
