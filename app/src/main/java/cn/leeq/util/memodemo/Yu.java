package cn.leeq.util.memodemo;


import java.util.List;

import cn.leeq.util.memodemo.bean.Status;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Yu {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Status>> withYu(
            @Path("owner") String owner,
            @Path("repo") String repo
    );
}
